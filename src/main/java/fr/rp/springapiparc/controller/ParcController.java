package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.ParcInDto;
import fr.rp.springapiparc.dto.out.ParcDetailOutDto;
import fr.rp.springapiparc.dto.out.ParcOutDto;
import fr.rp.springapiparc.entity.LieuEntity;
import fr.rp.springapiparc.entity.ParcEntity;
import fr.rp.springapiparc.entity.ParkingEntity;
import fr.rp.springapiparc.repository.LieuRepository;
import fr.rp.springapiparc.repository.ParcRepository;
import fr.rp.springapiparc.repository.ParkingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parcs")
public class ParcController {

    @Autowired
    private ParcRepository parcRepository;

    @Autowired
    private LieuRepository lieuRepository;

    @Autowired
    private ParkingRepository parkingRepository;



    @GetMapping("")
    @Operation(summary = "Affiche la liste des parcs", description = "Retourne une liste de parc",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Liste Parc")
            })
    public ResponseEntity<List<ParcOutDto>> getListParc() {
        List<ParcEntity> listParcEntity = parcRepository.findAll();
        List<ParcOutDto> listParcOutDto = new ArrayList<>();
        for (ParcEntity parcEntity : listParcEntity) {
            ParcOutDto parcOutDto = new ParcOutDto(parcEntity);
            listParcOutDto.add(parcOutDto);
        }
        return new ResponseEntity<>(listParcOutDto, HttpStatus.OK);
    }

    @GetMapping("/{idParc}")
    @Operation(summary = "le détail d'un parc par son Id", description = "Retourne le détail d'un parc",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Parc", content = @Content(schema = @Schema(implementation = ParcDetailOutDto.class))),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content)
            })
    public ResponseEntity<?> getParcById(@PathVariable Integer idParc) {
        Optional<ParcEntity> optionalParcEntity = parcRepository.findById(idParc);
        if (optionalParcEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Parc non trouvé");
        }
        ParcEntity parcEntity = optionalParcEntity.get();
        ParcDetailOutDto parcDetailOutDto = new ParcDetailOutDto(parcEntity);
        return ResponseEntity.ok(parcDetailOutDto);
    }

    @PostMapping("{idLieu}/{idParking}")
    @Transactional
    @Operation(summary = "Crée un parc parc son idLieu et idParking", description = "Crée un parc parc son idLieu et idParking",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Parc", content = @Content(schema = @Schema(implementation = ParcDetailOutDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            })
    public ResponseEntity<?> createParcByLieuAndIdParking(@PathVariable Integer idLieu, @PathVariable Integer idParking, @Valid @RequestBody ParcInDto parcInDto) {
        try {
        Optional<LieuEntity> optionalLieuEntity = lieuRepository.findById(idLieu);
        if (optionalLieuEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lieu non trouvé");
        }
        Optional<ParkingEntity> optionalParkingEntity = parkingRepository.findById(idParking);
        if (optionalParkingEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Parking non trouvé");
        }
            LieuEntity lieuEntity = optionalLieuEntity.get();
            ParkingEntity parkingEntity = optionalParkingEntity.get();
            ParcEntity parcEntity = new ParcEntity(parcInDto, lieuEntity, parkingEntity);
            parcRepository.save(parcEntity);
            ParcDetailOutDto parcDetailOutDto = new ParcDetailOutDto(parcEntity);
            return new ResponseEntity<>(parcDetailOutDto, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



















}