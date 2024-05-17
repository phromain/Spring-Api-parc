package dto.out;

import entity.TypeParcEntity;

public class TypeParcOutDto {

    private String libelleTypeParc;
    public TypeParcOutDto(TypeParcEntity typeParcEntity) {
        this.libelleTypeParc = typeParcEntity.getLibelleTypeParc();
    }
}
