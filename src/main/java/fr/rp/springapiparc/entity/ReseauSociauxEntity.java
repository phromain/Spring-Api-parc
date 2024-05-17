package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "reseau_sociaux")
public class ReseauSociauxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reseau_sociaux", nullable = false)
    private Integer id;

    @Column(name = "lib_reseau", length = 50)
    private String libReseau;

    @OneToMany(mappedBy = "idReseauSociaux")
    private Set<AbonnerEntity> abonners = new LinkedHashSet<>();

}