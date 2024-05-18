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
public class ParkingInDto {
    @Size(max = 50)
    @NotNull(message = "Le parking ne peut pas être null")
    @NotBlank(message = "Le parking ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    private String parking;

    public ParkingInDto(String parking) {
        this.parking = parking;
    }


}
