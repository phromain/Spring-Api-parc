package fr.rp.springapiparc.dto.out;

import fr.rp.springapiparc.entity.ParkingEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingOutDto {
    private Integer id;
    private String parking;


    public ParkingOutDto(ParkingEntity parking) {
        this.id = parking.getId();
        this.parking = parking.getParking();
    }

}
