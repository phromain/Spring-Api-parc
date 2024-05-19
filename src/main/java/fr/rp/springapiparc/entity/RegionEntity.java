package fr.rp.springapiparc.entity;

import fr.rp.springapiparc.dto.in.RegionInDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "region")
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_region", nullable = false)
    private Integer id;

    @Column(name = "nom_region", nullable = false, length = 50, unique = true)
    private String nomRegion;

    @OneToMany(mappedBy = "idRegion")
    private Set<LieuEntity> lieus = new LinkedHashSet<>();

    // contructs

    public RegionEntity(RegionInDto regionInDto) {
        this.nomRegion = regionInDto.getNomRegion();
    }

}