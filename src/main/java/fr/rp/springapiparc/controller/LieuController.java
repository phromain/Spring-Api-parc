package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.in.LieuInDto;
import fr.rp.springapiparc.dto.out.LieuOutDto;
import fr.rp.springapiparc.entity.LieuEntity;
import fr.rp.springapiparc.repository.LieuRepository;
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
@RequestMapping("/lieux")
public class LieuController {

    @Autowired
    private LieuRepository lieuRepository;

    @GetMapping("")
    @Operation(summary = "Affiche la liste des lieux", description = "Retourne une liste de lieux",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Liste de Lieux")
            })
    public ResponseEntity<List<LieuOutDto>> getListLieux() {
        List<LieuEntity> listLieuEntity = lieuRepository.findAll();
        List<LieuOutDto> listLieuOutDto = new ArrayList<>();
        for (LieuEntity lieuEntity : listLieuEntity) {
            LieuOutDto lieuOutDto = new LieuOutDto(lieuEntity);
            listLieuOutDto.add(lieuOutDto);
        }
        return new ResponseEntity<>(listLieuOutDto, HttpStatus.OK);
    }

    @GetMapping("/{idLieu}")
    @Operation(summary = "le détail d'une lieu par son Id", description = "Retourne le détail d'un lieu",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Détail Lieu", content = @Content(schema = @Schema(implementation = LieuOutDto.class))),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content)
            })
    public ResponseEntity<?> getLieuById(@PathVariable Integer idLieu) {
        Optional<LieuEntity> optionalLieuEntity = lieuRepository.findById(idLieu);
        if (optionalLieuEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lieu non trouvé");
        }
        LieuEntity lieuEntity = optionalLieuEntity.get();
        LieuOutDto lieuOutDto = new LieuOutDto(lieuEntity);
        return ResponseEntity.ok(lieuOutDto);
    }

    @PutMapping("/{idLieu}")
    @Transactional
    @Operation(summary = "Modifie un lieu", description = "Modifie un lieu",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Lieu mis à jour",  content = @Content(schema = @Schema(implementation = LieuInDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur Validator", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue", content = @Content)
            }
    )
    public ResponseEntity<?> updateLieu (@PathVariable Integer idLieu, @Valid @RequestBody LieuInDto lieuInDto) {
        try {
            Optional<LieuEntity> optionalLieuEntity = lieuRepository.findById(idLieu);
            if (optionalLieuEntity.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Lieu non trouvé");
            }
            LieuEntity lieuEntity = optionalLieuEntity.get();
            lieuEntity.insertUpdateValues(lieuInDto);
            lieuRepository.save(lieuEntity);
            LieuOutDto lieuOutDto = new LieuOutDto(lieuEntity);
            return new ResponseEntity<>(lieuOutDto, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue ");
        }
    }

    @DeleteMapping("/{idLieu}")
    @Transactional
    @Operation(summary = "Supprime un lieu", description = "Supprime un lieu",
            responses = {
                    @ApiResponse(responseCode = "200", description = " Lieu Supprimé", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lieu non trouvé", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Une erreur interne est survenue",content = @Content)
            }
    )
    public ResponseEntity<?> deleteLieu (@PathVariable Integer idLieu) {
        Optional<LieuEntity> optionalLieuEntity = lieuRepository.findById(idLieu);
        if (optionalLieuEntity.isEmpty()){
            return new ResponseEntity<>("Lieu non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            lieuRepository.deleteById(idLieu);
            return new ResponseEntity<>("Lieu Supprimé", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Une erreur est survenue ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
