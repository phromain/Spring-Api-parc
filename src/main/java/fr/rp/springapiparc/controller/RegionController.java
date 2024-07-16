package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.LieuInDto;
import fr.rp.springapiparc.dto.in.RegionInDto;
import fr.rp.springapiparc.dto.out.LieuOutDto;
import fr.rp.springapiparc.dto.out.RegionOutDto;
import fr.rp.springapiparc.entity.LieuEntity;
import fr.rp.springapiparc.entity.RegionEntity;
import fr.rp.springapiparc.repository.LieuRepository;
import fr.rp.springapiparc.repository.RegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import fr.rp.springapiparc.rest.ApikeyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/regions")
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private LieuRepository lieuRepository;

    @Autowired
    private ApikeyService apikeyService;

    @GetMapping("")
    @Operation(summary = "Affiche la liste des regions", description = "Retourne une liste de region",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " Liste Region",
                            content = @Content(
                                    schema = @Schema(implementation = RegionOutDto[].class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Exemple de réponse",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"nomRegion\": \"Test 1\",\n" +
                                                            "    \"slugRegion\": \"test-1\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"nomRegion\": \"Test 2\",\n" +
                                                            "    \"slugRegion\": \"test-2\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"id\": 3,\n" +
                                                            "    \"nomRegion\": \"Test 3\",\n" +
                                                            "    \"slugRegion\": \"test-3\"\n" +
                                                            "  }\n" + // Retirer la virgule en trop ici
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content)
            })
    public ResponseEntity<?> getListRegion(@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
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
                    @ApiResponse(responseCode = "404", description = "Region non trouvée", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content)

            })
    public ResponseEntity<?> getRegionById(@PathVariable Integer idRegion,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
        if (optionalRegionEntity.isEmpty()){
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
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionInDto regionInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            RegionEntity regionEntity = new RegionEntity(regionInDto);
            regionRepository.save(regionEntity);
            RegionOutDto regionOutDto = new RegionOutDto(regionEntity);
            return new ResponseEntity<>(regionOutDto, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{idRegion}/lieu")
    @Transactional
    @Operation(summary = "Crée un lieu par un IDregion", description = "Crée un lieu par un IDregion",
            responses = {
                    @ApiResponse(responseCode = "201", description = " Lieu Créer", content = @Content(schema = @Schema(implementation = RegionInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Region non trouvée", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createLieuByIdRegion(@PathVariable Integer idRegion, @Valid @RequestBody LieuInDto lieuInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<RegionEntity> optionalRegionEntity;
        try {
            optionalRegionEntity = regionRepository.findById(idRegion);
            if (optionalRegionEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Région non trouvé");
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            RegionEntity regionEntity = optionalRegionEntity.get();
            LieuEntity lieuEntity = new LieuEntity(lieuInDto, regionEntity);
            lieuRepository.save(lieuEntity);
            LieuOutDto lieuOutDto = new LieuOutDto(lieuEntity);
            return new ResponseEntity<>(lieuOutDto, HttpStatus.CREATED);
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
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Région non trouvé", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateRegion (@PathVariable Integer idRegion, @Valid @RequestBody RegionInDto regionInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
            if (optionalRegionEntity.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Région non trouvée");
            }
            RegionEntity regionEntity = optionalRegionEntity.get();
            regionEntity.setNomRegion(regionInDto.getNomRegion());
            regionRepository.save(regionEntity);
            RegionOutDto regionOutDto = new RegionOutDto(regionEntity);
            return new ResponseEntity<>(regionOutDto, HttpStatus.OK);
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
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteRegion (@PathVariable Integer idRegion,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(idRegion);
        if (optionalRegionEntity.isEmpty()){
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
