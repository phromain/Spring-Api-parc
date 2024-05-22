package fr.rp.springapiparc.dto.out;

import com.github.slugify.Slugify;
import fr.rp.springapiparc.entity.TypeParcEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeParcOutDto {
    private Integer id;
    private String libelleTypeParc;
    private String slugType;

    public TypeParcOutDto(TypeParcEntity typeParcEntity) {
        this.id = typeParcEntity.getId();
        this.libelleTypeParc = typeParcEntity.getLibelleTypeParc();

        final Slugify slg = Slugify.builder().build();

        this.slugType = slg.slugify(typeParcEntity.getLibelleTypeParc());
    }
}
