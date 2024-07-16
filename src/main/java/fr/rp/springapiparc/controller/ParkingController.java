package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.ParkingInDto;
import fr.rp.springapiparc.dto.out.ParkingOutDto;
import fr.rp.springapiparc.entity.ParkingEntity;
import fr.rp.springapiparc.repository.ParkingRepository;
import fr.rp.springapiparc.rest.ApikeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/parkings")
public class ParkingController {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ApikeyService apikeyService;

    @GetMapping("")
    @Operation(summary = "Affiche la liste des types de parkings", description = "Affiche la liste des types de parkings",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " Liste des types de Parkings",
                            content = @Content(
                                    schema = @Schema(implementation = ParkingOutDto[].class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Exemple de réponse",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"parking\": \"Gratuit\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"parking\": \"Payant\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"id\": 3,\n" +
                                                            "    \"parking\": \"Pas d'informations\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content)
            })
    public ResponseEntity<?> getListParking(@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        List<ParkingEntity> listParkingEntity = parkingRepository.findAll();
        List<ParkingOutDto> listParkingOutDto = new ArrayList<>();
        for (ParkingEntity parkingEntity : listParkingEntity) {
            ParkingOutDto parkingOutDto = new ParkingOutDto(parkingEntity);
            listParkingOutDto.add(parkingOutDto);
        }
        return new ResponseEntity<>(listParkingOutDto, HttpStatus.OK);
    }

    @GetMapping("/{idParking}")
    @Operation(summary = "le détail d'un parking par son Id", description = "Retourne le détail d'un parking",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Parking", content = @Content(schema = @Schema(implementation = ParkingOutDto.class))),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parking non trouvé", content = @Content)
            })
    public ResponseEntity<?> getParkingById(@PathVariable Integer idParking,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<ParkingEntity> optionalParkingEntity = parkingRepository.findById(idParking);
        if (optionalParkingEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Parking non trouvé");
        }
        ParkingEntity parkingEntity = optionalParkingEntity.get();
        ParkingOutDto parkingOutDto = new ParkingOutDto(parkingEntity);
        return ResponseEntity.ok(parkingOutDto);
    }

    @PostMapping("")
    @Transactional
    @Operation(summary = "Crée un parking", description = "Crée un parking",
            responses = {
                    @ApiResponse(responseCode = "201", description = " Parking Créer", content = @Content(schema = @Schema(implementation = ParkingInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createType(@Valid @RequestBody ParkingInDto parkingInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            ParkingEntity parkingEntity = new ParkingEntity(parkingInDto);
            parkingRepository.save(parkingEntity);
            ParkingOutDto parkingOutDto = new ParkingOutDto(parkingEntity);
            return new ResponseEntity<>(parkingOutDto, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{idParking}")
    @Transactional
    @Operation(summary = "Modifie un parking", description = "Modifie un parking",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Parking mis à jour", content = @Content(schema = @Schema(implementation = ParkingInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parking non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateParking(@PathVariable Integer idParking, @Valid @RequestBody ParkingInDto parkingInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            Optional<ParkingEntity> optionalParkingEntity = parkingRepository.findById(idParking);
            if (optionalParkingEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Parking non trouvé");
            }
            ParkingEntity parkingEntity = optionalParkingEntity.get();
            parkingEntity.setParking(parkingInDto.getParking());
            parkingRepository.save(parkingEntity);
            ParkingOutDto parkingOutDto = new ParkingOutDto(parkingEntity);
            return new ResponseEntity<>(parkingOutDto, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }

    @DeleteMapping("/{idParking}")
    @Transactional
    @Operation(summary = "Supprime un parking", description = "Supprime un parking",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Parking Supprimé", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parking non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> deleteParking(@PathVariable Integer idParking,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<ParkingEntity> optionalParkingEntity = parkingRepository.findById(idParking);
        if (optionalParkingEntity.isEmpty()) {
            return new ResponseEntity<>("Parking non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            parkingRepository.deleteById(idParking);
            return new ResponseEntity<>("Parking Supprimé", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

