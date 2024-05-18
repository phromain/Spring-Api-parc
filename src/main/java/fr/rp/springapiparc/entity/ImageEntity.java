package fr.rp.springapiparc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_img", nullable = false)
    private Integer id;

    @Column(name = "ref_img", nullable = false, length = 250)
    private String refImg;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_parc", nullable = false)
    private ParcEntity idParc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_attribut_img", nullable = false)
    private AttributImgEntity idAttributImg;

}