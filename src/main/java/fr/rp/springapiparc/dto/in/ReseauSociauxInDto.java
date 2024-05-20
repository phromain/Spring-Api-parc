package fr.rp.springapiparc.dto.in;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReseauSociauxInDto {
    @Size(max = 50)
    @NotBlank(message = "Le nom du réseau social ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    private String libelleReseau;

    public ReseauSociauxInDto(String libelleReseau) {
        this.libelleReseau = libelleReseau;
    }
}
