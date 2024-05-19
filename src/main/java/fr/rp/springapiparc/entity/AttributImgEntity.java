package fr.rp.springapiparc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "attribut_img")
public class AttributImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attribut_img", nullable = false)
    private Integer id;

    @Column(name = "lib_attribut_img", nullable = false, length = 50, unique = true)
    private String libAttributImg;

    @OneToMany(mappedBy = "idAttributImg")
    private Set<ImageEntity> images = new LinkedHashSet<>();

}