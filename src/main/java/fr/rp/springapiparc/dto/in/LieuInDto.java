package fr.rp.springapiparc.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LieuInDto {

    @Size(max = 250)
    @NotBlank(message = "La ville ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    @NotNull(message = "La ville ne peut pas être nulle")
    private String ville;

    @NotNull(message = "Le code postal ne peut pas être nul")
    @NotBlank(message = "Le code postal ne peut pas être vide")
    @Pattern(regexp = "^(0[1-9]|[1-4][0-9]|5[0-2]|6[0-9]|7[0-5]|8[0-9]|9[0-5])[0-9]{3}$"
            , message = "Le code postal n'est pas correct et il doit contenir uniquement 5 chiffres")
    @Size(max = 5, message = "Le code postal doit avoir une longueur de 5 caractères")
    private String codePostal;

    // Contructs
    public LieuInDto(String ville, String codePostal) {
        this.ville = ville;
        this.codePostal = codePostal;
    }

}
