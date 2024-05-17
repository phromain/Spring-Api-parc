package controller;


import dto.out.TypeParcOutDto;
import entity.TypeParcEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.TypeParcRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/type")
public class TypeParcController {

    @Autowired
    private TypeParcRepository typeParcRepository;

    @GetMapping("")
    @Operation(summary = "Affiche la liste des typeParcs", description = "Retourne une liste de typeParc")
    @ApiResponse(responseCode = "200", description = " Liste TypeParc")
    public ResponseEntity<List<TypeParcOutDto>> getListTypeParc() {
        List<TypeParcEntity> listTypeParc = typeParcRepository.findAll();
        List<TypeParcOutDto> listTypeParcDto = listTypeParc.stream()
                .map(TypeParcOutDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listTypeParcDto);
    }



    public List<TypeParcOutDto> getListTypeParc2() {
        return typeParcRepository.findAll().stream()
                .map(TypeParcOutDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idType}")
    public ResponseEntity<TypeParcEntity> getTypeParcById(@PathVariable Integer idType) {
        return typeParcRepository.findById(idType)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoResultException("Cet identifiant n'existe pas !"));

    }


}
