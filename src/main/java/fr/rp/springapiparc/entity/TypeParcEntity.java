package fr.rp.springapiparc.entity;

import fr.rp.springapiparc.dto.in.TypeParcInDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "type_parc")
public class TypeParcEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type", nullable = false)
    private Integer id;

    @Column(name = "libelle_type_parc", nullable = false, length = 20, unique = true)
    private String libelleTypeParc;

    public TypeParcEntity(TypeParcInDto typeParcInDto) {
        this.libelleTypeParc = typeParcInDto.getLibelleTypeParc();
    }

}