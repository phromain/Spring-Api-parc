package fr.rp.springapiparc.entity;

import fr.rp.springapiparc.dto.in.ReseauSociauxInDto;
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
@Table(name = "reseau_sociaux")
public class ReseauSociauxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reseau_sociaux", nullable = false)
    private Integer id;

    @Column(name = "lib_reseau", length = 50, unique = true)
    private String libReseau;

    @OneToMany(mappedBy = "idReseauSociaux")
    private Set<AbonnerEntity> abonners = new LinkedHashSet<>();

    public ReseauSociauxEntity(ReseauSociauxInDto reseauSociauxInDto) {
        this.libReseau = reseauSociauxInDto.getLibelleReseau();
    }




}