package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "region")
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_region", nullable = false)
    private Integer id;

    @Column(name = "nom_region", nullable = false, length = 50)
    private String nomRegion;

    @OneToMany(mappedBy = "idRegion")
    private Set<LieuEntity> lieus = new LinkedHashSet<>();

}