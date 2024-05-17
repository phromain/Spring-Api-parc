package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "parking")
public class ParkingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parking", nullable = false)
    private Integer id;

    @Column(name = "parking", nullable = false, length = 50)
    private String parking;

    @OneToMany(mappedBy = "idParking")
    private Set<ParcEntity> parcs = new LinkedHashSet<>();

}