package fr.rp.springapiparc.entity;

import fr.rp.springapiparc.dto.in.LieuInDto;
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
@Table(name = "lieu")
public class LieuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lieu", nullable = false)
    private Integer id;

    @Column(name = "ville", nullable = false, length = 250)
    private String ville;

    @Column(name = "code_postal", nullable = false, length = 5)
    private String codePostal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_region", nullable = false)
    private RegionEntity idRegion;

    @OneToMany(mappedBy = "idLieu")
    private Set<ParcEntity> parcs = new LinkedHashSet<>();

    public void insertUpdateValuesLieu(LieuInDto lieuInDto) {
        this.ville = lieuInDto.getVille();
        this.codePostal = lieuInDto.getCodePostal();
    }

    public LieuEntity(LieuInDto lieuInDto, RegionEntity regionEntity) {
        this.ville = lieuInDto.getVille();
        this.codePostal = lieuInDto.getCodePostal();
        this.idRegion = regionEntity;
    }
}