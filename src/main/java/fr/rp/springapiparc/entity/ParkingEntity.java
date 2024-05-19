package fr.rp.springapiparc.entity;

import fr.rp.springapiparc.dto.in.ParkingInDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parking")
public class ParkingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parking", nullable = false)
    private Integer id;

    @Column(name = "parking", nullable = false, length = 50, unique = true)
    private String parking;

    @OneToMany(mappedBy = "idParking")
    private Set<ParcEntity> parcs = new LinkedHashSet<>();

    public ParkingEntity(ParkingInDto parkingInDto) {
        this.parking = parkingInDto.getParking();
    }

}