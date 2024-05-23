package fr.rp.springapiparc.controller;


import fr.rp.springapiparc.dto.in.ReseauSociauxInDto;
import fr.rp.springapiparc.dto.in.TypeParcInDto;
import fr.rp.springapiparc.dto.out.ReseauSociauxOutDto;
import fr.rp.springapiparc.entity.ReseauSociauxEntity;
import fr.rp.springapiparc.repository.ReseauSociauxRepository;
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
@RequestMapping("/reseausociaux")
public class ReseauSociauxController {

    @Autowired
    private ReseauSociauxRepository reseauSociauxRepository;


    @GetMapping("")
    @Operation(summary = "Affiche la liste des reseauSociauxs", description = "Retourne une liste des reseauSociauxs",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Liste ReseauSociaux")

            })
    public ResponseEntity<List<ReseauSociauxOutDto>> getListReseauSociaux() {
        List<ReseauSociauxEntity> listReseauSociauxEntity = reseauSociauxRepository.findAll();
        List<ReseauSociauxOutDto> listReseauSociauxOutDto = new ArrayList<>();
        for (ReseauSociauxEntity reseauSociauxEntity : listReseauSociauxEntity) {
            ReseauSociauxOutDto reseauSociauxOutDto = new ReseauSociauxOutDto(reseauSociauxEntity);
            listReseauSociauxOutDto.add(reseauSociauxOutDto);
        }
        return new ResponseEntity<>(listReseauSociauxOutDto, HttpStatus.OK);
    }



    @GetMapping("/{idReseauSociaux}")
    @Operation(summary = "le détail d'un reseauSociaux par son Id", description = "Retourne le détail d'un reseauSociaux",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail ReseauSociaux", content = @Content(schema = @Schema(implementation = ReseauSociauxOutDto.class))),
                    @ApiResponse(responseCode = "404", description = "ReseauSociaux non trouvé", content = @Content)
            })
    public ResponseEntity<?> getReseauSociauxById(@PathVariable Integer idReseauSociaux) {
        Optional<ReseauSociauxEntity> optionalReseauSociauxEntity = reseauSociauxRepository.findById(idReseauSociaux);
        if (optionalReseauSociauxEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ReseauSociaux non trouvé");
        }
        ReseauSociauxEntity reseauSociauxEntity = optionalReseauSociauxEntity.get();
        ReseauSociauxOutDto reseauSociauxOutDto = new ReseauSociauxOutDto(reseauSociauxEntity);
        return ResponseEntity.ok(reseauSociauxOutDto);
    }

    @PostMapping("")
    @Transactional
    @Operation(summary = "Crée un reseauSociaux", description = "Crée un reseauSociaux",
            responses = {
                    @ApiResponse(responseCode = "201", description = " ReseauSociaux Créer", content = @Content(schema = @Schema(implementation = TypeParcInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> createReseauSociaux(@Valid @RequestBody ReseauSociauxInDto reseauSociauxInDto) {
        try {
            ReseauSociauxEntity reseauSociauxEntity = new ReseauSociauxEntity(reseauSociauxInDto);
            reseauSociauxRepository.save(reseauSociauxEntity);
            ReseauSociauxOutDto reseauSociauxOutDto = new ReseauSociauxOutDto(reseauSociauxEntity);
            return new ResponseEntity<>(reseauSociauxOutDto, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{idReseauSociaux}")
    @Transactional
    @Operation(summary = "Modifie un reseauSociaux", description = "Modifie un reseauSociaux",
            responses = {
                    @ApiResponse(responseCode = "200", description = " ReseauSociaux mis à jour",  content = @Content(schema = @Schema(implementation = ReseauSociauxInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "ReseauSociaux non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateReseauSociaux (@PathVariable Integer idReseauSociaux, @Valid @RequestBody ReseauSociauxInDto reseauSociauxInDto) {
        try {
            Optional<ReseauSociauxEntity> optionalReseauSociauxEntity = reseauSociauxRepository.findById(idReseauSociaux);
            if (optionalReseauSociauxEntity.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("ReseauSociaux non trouvé");
            }
            ReseauSociauxEntity reseauSociauxEntity = optionalReseauSociauxEntity.get();
            reseauSociauxEntity.setLibReseau(reseauSociauxInDto.getLibelleReseau());
            reseauSociauxRepository.save(reseauSociauxEntity);
            ReseauSociauxOutDto reseauSociauxOutDto = new ReseauSociauxOutDto(reseauSociauxEntity);
            return new ResponseEntity<>(reseauSociauxOutDto, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }

    @DeleteMapping("/{idReseauSociaux}")
    @Transactional
    @Operation(summary = "Supprime un reseauSociaux", description = "Supprime un reseauSociaux",
            responses = {
                    @ApiResponse(responseCode = "200", description = " ReseauSociaux Supprimé", content = @Content),
                    @ApiResponse(responseCode = "404", description = "ReseauSociaux non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteReseauSociaux (@PathVariable Integer idReseauSociaux) {
        Optional<ReseauSociauxEntity> optionalReseauSociauxEntity = reseauSociauxRepository.findById(idReseauSociaux);
        if (optionalReseauSociauxEntity.isEmpty()){
            return new ResponseEntity<>("ReseauSociaux non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            reseauSociauxRepository.deleteById(idReseauSociaux);
            return new ResponseEntity<>("ReseauSociaux Supprimé", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
