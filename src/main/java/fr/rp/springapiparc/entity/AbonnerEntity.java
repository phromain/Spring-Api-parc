package fr.rp.springapiparc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "abonner")
public class AbonnerEntity {
    @EmbeddedId
    private AbonnerEntityId id;

    @MapsId("idReseauSociaux")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reseau_sociaux", nullable = false)
    private ReseauSociauxEntity idReseauSociaux;

    @MapsId("idParc")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_parc", nullable = false)
    private ParcEntity idParc;

    @Column(name = "url", nullable = false, length = 250)
    private String url;

}