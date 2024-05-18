package fr.rp.springapiparc.dto.out;

import fr.rp.springapiparc.entity.LieuEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LieuOutDto {

    private Integer id;
    private String ville;
    private String codePostal;
    private String region;

    public LieuOutDto(LieuEntity lieuEntity) {
        this.id = lieuEntity.getId();
        this.ville = lieuEntity.getVille();
        this.codePostal = lieuEntity.getCodePostal();
        this.region = lieuEntity.getIdRegion().getNomRegion();
    }

}
