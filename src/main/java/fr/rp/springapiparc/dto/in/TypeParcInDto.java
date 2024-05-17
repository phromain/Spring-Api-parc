package dto.in;

import entity.TypeParcEntity;

public class TypeParcInDto {

    private String libelleTypeParc;
    public TypeParcInDto(TypeParcEntity typeParcEntity) {
        this.libelleTypeParc = typeParcEntity.getLibelleTypeParc();
    }
}
