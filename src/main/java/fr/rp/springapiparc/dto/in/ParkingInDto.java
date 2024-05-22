package fr.rp.springapiparc.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParkingInDto {
    @Size(max = 50)
    @NotBlank(message = "Le parking ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    @JsonProperty("parking")
    private String parking;

    public ParkingInDto(String parking) {
        this.parking = parking;
    }


}
