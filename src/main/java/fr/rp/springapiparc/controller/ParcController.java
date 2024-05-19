package fr.rp.springapiparc.controller;

import fr.rp.springapiparc.dto.out.ParcDetailOutDto;
import fr.rp.springapiparc.dto.out.ParcOutDto;
import fr.rp.springapiparc.entity.ParcEntity;
import fr.rp.springapiparc.repository.ParcRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parcs")
public class ParcController {

    @Autowired
    private ParcRepository parcRepository;


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
}