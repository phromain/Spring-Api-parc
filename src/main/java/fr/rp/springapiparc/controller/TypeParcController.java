package fr.rp.springapiparc.controller;


import fr.rp.springapiparc.dto.in.TypeParcInDto;
import fr.rp.springapiparc.dto.out.TypeParcOutDto;
import fr.rp.springapiparc.entity.TypeParcEntity;
import fr.rp.springapiparc.repository.TypeParcRepository;
import fr.rp.springapiparc.service.ApikeyService;
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
@RequestMapping("/types")
public class TypeParcController {

    @Autowired
    private TypeParcRepository typeParcRepository;

    @Autowired
    private ApikeyService apikeyService;
    @GetMapping("")
    @Operation(summary = "Affiche la liste des typeParcs", description = "Retourne une liste de typeParc",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste TypeParc",
                            content = @Content(
                                    schema = @Schema(implementation = TypeParcOutDto[].class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Exemple de réponse",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"libelleTypeParc\": \"Attraction\",\n" +
                                                            "    \"slugType\": \"attraction\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"libelleTypeParc\": \"Aquatique\",\n" +
                                                            "    \"slugType\": \"aquatique\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content)
            })
    public ResponseEntity<?> getListTypeParc(@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        List<TypeParcEntity> listTypeParcEntity = typeParcRepository.findAll();
        List<TypeParcOutDto> listTypeParcOutDto = new ArrayList<>();
        for (TypeParcEntity typeParcEntity : listTypeParcEntity) {
            TypeParcOutDto typeParcOutDto = new TypeParcOutDto(typeParcEntity);
            listTypeParcOutDto.add(typeParcOutDto);
        }
        return new ResponseEntity<>(listTypeParcOutDto, HttpStatus.OK);
    }

    @GetMapping("/{idType}")
    @Operation(summary = "le type par son Id", description = "Retourne le type",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Type trouvé", content = @Content(schema = @Schema(implementation = TypeParcOutDto.class))),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Type non trouvé", content = @Content)
            })
    public ResponseEntity<?> getTypeParcById(@PathVariable Integer idType,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<TypeParcEntity> optionalTypeParcEntity = typeParcRepository.findById(idType);
        if (optionalTypeParcEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Type non trouvé");
        }
        TypeParcEntity typeParcEntity = optionalTypeParcEntity.get();
        TypeParcOutDto typeParcOutDto = new TypeParcOutDto(typeParcEntity);
        return ResponseEntity.ok(typeParcOutDto);
    }


    @PostMapping("")
    @Transactional
    @Operation(summary = "Crée un type", description = "Crée un type",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Type Créer", content = @Content(schema = @Schema(implementation = TypeParcInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createType(@Valid  @RequestBody TypeParcInDto typeParcInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            TypeParcEntity typeParcEntity = new TypeParcEntity(typeParcInDto);
            typeParcRepository.save(typeParcEntity);
            TypeParcOutDto typeParcOutDto = new TypeParcOutDto(typeParcEntity);
            return new ResponseEntity<>(typeParcOutDto, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{idType}")
    @Transactional
    @Operation(summary = "Modifie un type", description = "Modifie un type",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Type mis à jour",  content = @Content(schema = @Schema(implementation = TypeParcInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Type non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateTypeParc (@PathVariable Integer idType, @Valid @RequestBody TypeParcInDto typeParcInDto,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        try {
            Optional<TypeParcEntity> optionalTypeParcEntity = typeParcRepository.findById(idType);
            if (optionalTypeParcEntity.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Type non trouvé");
            }
            TypeParcEntity typeParcEntity = optionalTypeParcEntity.get();
            typeParcEntity.setLibelleTypeParc(typeParcInDto.getLibelleTypeParc());
            typeParcRepository.save(typeParcEntity);
            TypeParcOutDto typeParcOutDto = new TypeParcOutDto(typeParcEntity);
            return new ResponseEntity<>(typeParcOutDto, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }

    @DeleteMapping("/{idType}")
    @Transactional
    @Operation(summary = "Supprime un type", description = "Supprime un type",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Type Supprimé", content = @Content),
                    @ApiResponse(responseCode = "401", description = "apikey non valide", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Type non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteType (@PathVariable Integer idType,@RequestHeader(value = "apikey", required = true) String apikey)  {
        if (!apikeyService.validateApiKey(apikey)) {
            return new ResponseEntity<>("apikey non valide", HttpStatus.UNAUTHORIZED);
        }
        Optional<TypeParcEntity> optionalTypeParcEntity = typeParcRepository.findById(idType);
        if (optionalTypeParcEntity.isEmpty()){
            return new ResponseEntity<>("Type non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            typeParcRepository.deleteById(idType);
            return new ResponseEntity<>("Suppression du type", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
