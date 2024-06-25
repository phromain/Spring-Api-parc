package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.ParcInDto;
import fr.rp.springapiparc.dto.out.*;
import fr.rp.springapiparc.entity.*;
import fr.rp.springapiparc.repository.*;
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
@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/{nomParc}")
    @Operation(summary = "le détail d'un parc par son nom", description = "Retourne le détail d'un parc",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Parc", content = @Content(schema = @Schema(implementation = ParcDetailOutDto.class))),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content)
            })
    public ResponseEntity<?> getParcByNom(@PathVariable String idParc) {
        Optional<ParcEntity> optionalParcEntity = parcRepository.findByNomParc(idParc);
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

    @PutMapping("/{idParc}")
    @Transactional
    @Operation(summary = "Modifie un type", description = "Modifie un type",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Parc mis à jour",  content = @Content(schema = @Schema(implementation = ParcInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateParc (@PathVariable Integer idParc, @Valid @RequestBody ParcInDto parcInDto) {
        try {
            Optional<ParcEntity> optionalParcEntity = parcRepository.findById(idParc);
            if (optionalParcEntity.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Parc non trouvé");
            }
            ParcEntity parcEntity = optionalParcEntity.get();
            parcEntity.insertUpdateValuesParc(parcInDto);
            parcRepository.save(parcEntity);
            ParcDetailOutDto parcDetailOutDto = new ParcDetailOutDto(parcEntity);
            return new ResponseEntity<>(parcDetailOutDto, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }





    @DeleteMapping("/{idParc}")
    @Transactional
    @Operation(summary = "Supprime un Parc", description = "Supprime un Parc",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parc Supprimé", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteParc (@PathVariable Integer idParc) {
        Optional<ParcEntity> optionalParcEntity = parcRepository.findById(idParc);
        if (optionalParcEntity.isEmpty()){
            return new ResponseEntity<>("Parc non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            parcRepository.deleteById(idParc);
            return new ResponseEntity<>("Parc Supprimé", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}