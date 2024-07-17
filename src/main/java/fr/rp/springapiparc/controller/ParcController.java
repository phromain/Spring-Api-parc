package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.ParcInDto;
import fr.rp.springapiparc.dto.out.*;
import fr.rp.springapiparc.entity.*;
import fr.rp.springapiparc.repository.*;
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

    @Autowired
    private ApikeyService apikeyService;


    @GetMapping("")
    @Operation(summary = "Affiche la liste des parcs", description = "Retourne une liste de parc",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " Liste Parc",
                            content = @Content(
                                    schema = @Schema(implementation = ParcOutDto[].class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Exemple de réponse",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"idParc\": 1,\n" +
                                                            "    \"nomParc\": \"Dennlys-parc\",\n" +
                                                            "    \"slugParc\": \"dennlys-parc\",\n" +
                                                            "    \"presentation\": \"Dennlys Parc fait sans aucun doute partie des meilleurs parcs d’attractions français destinés aux sorties en famille. Tout le monde y trouve de quoi combler ses envies, quel que soit son âge ou sa taille.\",\n" +
                                                            "    \"typeParc\": [\n" +
                                                            "      \"Attraction\"\n" +
                                                            "    ],\n" +
                                                            "    \"imagePrez\": [\n" +
                                                            "      \"prez-Dennlys-parc\"\n" +
                                                            "    ],\n" +
                                                            "    \"periodeOuverture\": [\n" +
                                                            "      \"2024-06-08 - 2024-06-09\",\n" +
                                                            "    ],\n" +
                                                            "    \"nomRegion\": \"Hauts-de-France\",\n" +
                                                            "    \"slugRegion\": \"hauts-de-france\",\n" +
                                                            "    \"idRegion\": 5,\n" +
                                                            "    \"parkingGratuit\": true,\n" +
                                                            "    \"restauration\": true,\n" +
                                                            "    \"boutique\": true,\n" +
                                                            "    \"sejour\": false,\n" +
                                                            "    \"transport\": false,\n" +
                                                            "    \"prixAdulte\": \"21.00\",\n" +
                                                            "    \"prixEnfant\": \"18.00\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"idParc\": 2,\n" +
                                                            "    \"nomParc\": \"La Mer de Sable\",\n" +
                                                            "    \"slugParc\": \"la-mer-de-sable\",\n" +
                                                            "    \"presentation\": \"la Mer de Sable est sans doute le plus vieux parc à thème en activité en France. Véritable coin de paradis pour les amateurs de l’univers des cow-boys. Partez à l’aventure de la Mer de Sable dans un cadre naturel exceptionnel…Des Portes du Désert au ...\",\n" +
                                                            "    \"typeParc\": [\n" +
                                                            "      \"Attraction\",\n" +
                                                            "      \"Spectacle\"\n" +
                                                            "    ],\n" +
                                                            "    \"imagePrez\": [\n" +
                                                            "      \"prez-La Mer de Sable\"\n" +
                                                            "    ],\n" +
                                                            "    \"periodeOuverture\": [\n" +
                                                            "      \"2024-09-07 - 2024-09-08\",\n" +
                                                            "      \"2024-05-08 - 2024-05-12\",\n" +
                                                            "    ],\n" +
                                                            "    \"nomRegion\": \"Hauts-de-France\",\n" +
                                                            "    \"slugRegion\": \"hauts-de-france\",\n" +
                                                            "    \"idRegion\": 5,\n" +
                                                            "    \"parkingGratuit\": true,\n" +
                                                            "    \"restauration\": true,\n" +
                                                            "    \"boutique\": true,\n" +
                                                            "    \"sejour\": false,\n" +
                                                            "    \"transport\": false,\n" +
                                                            "    \"prixAdulte\": \"30.50\",\n" +
                                                            "    \"prixEnfant\": \"25.00\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Parametre Authentification manquant", content = @Content),
                    @ApiResponse(responseCode = "403", description = "apikey non valide", content = @Content),
            })
    public ResponseEntity<?> getListParc(@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.FORBIDDEN);
        }
        List<ParcEntity> listParcEntity = parcRepository.findAll();
        List<ParcOutDto> listParcOutDto = new ArrayList<>();
        for (ParcEntity parcEntity : listParcEntity) {
            ParcOutDto parcOutDto = new ParcOutDto(parcEntity);
            listParcOutDto.add(parcOutDto);
        }
        return new ResponseEntity<>(listParcOutDto, HttpStatus.OK);
    }

    @GetMapping("/{slugParc}")
    @Operation(summary = "le détail d'un parc par son slug", description = "Retourne le détail d'un parc",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Parc", content = @Content(schema = @Schema(implementation = ParcDetailOutDto.class))),
                    @ApiResponse(responseCode = "401", description = "Parametre Authentification manquant", content = @Content),
                    @ApiResponse(responseCode = "403", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content)
            })
    public ResponseEntity<?> getParcByNom(@PathVariable String slugParc,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.FORBIDDEN);
        }
        Optional<ParcEntity> optionalParcEntity = parcRepository.findBySlugParc(slugParc);
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
                    @ApiResponse(responseCode = "401", description = "Parametre Authentification manquant", content = @Content),
                    @ApiResponse(responseCode = "403", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            })
    public ResponseEntity<?> createParcByLieuAndIdParking(@PathVariable Integer idLieu, @PathVariable Integer idParking, @Valid @RequestBody ParcInDto parcInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.FORBIDDEN);
        }
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
                    @ApiResponse(responseCode = "401", description = "Parametre Authentification manquant", content = @Content),
                    @ApiResponse(responseCode = "403", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateParc (@PathVariable Integer idParc, @Valid @RequestBody ParcInDto parcInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.FORBIDDEN);
        }
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
                    @ApiResponse(responseCode = "401", description = "Parametre Authentification manquant", content = @Content),
                    @ApiResponse(responseCode = "403", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Parc non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteParc (@PathVariable Integer idParc,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.FORBIDDEN);
        }
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