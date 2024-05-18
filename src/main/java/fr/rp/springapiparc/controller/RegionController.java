package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.RegionInDto;
import fr.rp.springapiparc.dto.out.RegionOutDto;
import fr.rp.springapiparc.entity.RegionEntity;
import fr.rp.springapiparc.repository.RegionRepository;
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
@RequestMapping("/regions")
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;


    @GetMapping("")
    @Operation(summary = "Affiche la liste des regions", description = "Retourne une liste de region",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Liste Region")
            })
    public ResponseEntity<List<RegionOutDto>> getListRegion() {
        List<RegionEntity> listRegion = regionRepository.findAll();
        List<RegionOutDto> listRegionDto = new ArrayList<>();
        for (RegionEntity regionEntity : listRegion) {
            RegionOutDto regionOutDto = new RegionOutDto(regionEntity);
            listRegionDto.add(regionOutDto);
        }
        return new ResponseEntity<>(listRegionDto, HttpStatus.OK);
    }

    @GetMapping("/{idRegion}")
    @Operation(summary = "le détail d'une region par son Id", description = "Retourne le détail d'une region",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Region",content = @Content(schema = @Schema(implementation = RegionOutDto.class))),
                    @ApiResponse(responseCode = "404", description = "Region non trouvée", content = @Content)
            })
    public ResponseEntity<?> getRegionById(@PathVariable Integer idRegion) {
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
        if (!optionalRegionEntity.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Region non trouvée");
        }
        RegionEntity regionEntity = optionalRegionEntity.get();
        RegionOutDto regionOutDto = new RegionOutDto(regionEntity);
        return ResponseEntity.ok(regionOutDto);
    }

    @PostMapping("")
    @Transactional
    @Operation(summary = "Crée une region", description = "Crée une region",
            responses = {
                    @ApiResponse(responseCode = "201", description = " Région Créer", content = @Content(schema = @Schema(implementation = RegionInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur indiquer", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionInDto regionInDto) {
        try {
            RegionEntity regionEntity = new RegionEntity(regionInDto);
            regionRepository.save(regionEntity);
            return new ResponseEntity<>(regionEntity, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{idRegion}")
    @Transactional
    @Operation(summary = "Modifie une region", description = "Modifie une region",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Région mis à jour",  content = @Content(schema = @Schema(implementation = RegionInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur indiquer ", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Région non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateRegion (@PathVariable Integer idRegion, @Valid @RequestBody RegionInDto regionInDto) {
        try {
            Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
            if (!optionalRegionEntity.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Région non trouvé");
            }
            RegionEntity regionEntity = optionalRegionEntity.get();
            regionEntity.setNomRegion(regionInDto.getNomRegion());
            regionRepository.save(regionEntity);
            return new ResponseEntity<>(regionEntity, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }

    @DeleteMapping("/{idRegion}")
    @Transactional
    @Operation(summary = "Supprime une region", description = "Supprime une region",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Région Supprimée", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Région non trouvée", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteRegion (@PathVariable Integer idRegion) {
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
        if (!optionalRegionEntity.isPresent()){
            return new ResponseEntity<>("Région non trouvée", HttpStatus.NOT_FOUND);
        }
        try {
            regionRepository.deleteById(idRegion);
            return new ResponseEntity<>("Région Supprimée", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
