package fr.rp.springapiparc.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionInDto {


    // Attributs
    @NotNull(message = "Le nom de région ne peut pas être nulle")
    @NotBlank(message = "Le nom de région ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    private String nomRegion;

    // Contructs

    public RegionInDto(String nomRegion) {
        this.nomRegion = nomRegion;
    }


    }