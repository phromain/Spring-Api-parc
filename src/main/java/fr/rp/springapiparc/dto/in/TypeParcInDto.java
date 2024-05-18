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
public class TypeParcInDto {

    @NotNull(message = "Le type ne peut pas être nulle")
    @NotBlank(message = "Le type ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    private String libelleTypeParc;

    public TypeParcInDto(String libelleTypeParc) {
        this.libelleTypeParc = libelleTypeParc;
    }


}
