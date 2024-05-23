package fr.rp.springapiparc.dto.out;

import fr.rp.springapiparc.entity.ReseauSociauxEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReseauSociauxOutDto {
    private Integer id;
    private String libelleReseau;

    public ReseauSociauxOutDto(ReseauSociauxEntity reseauSociauxEntity) {
        this.id = reseauSociauxEntity.getId();
        this.libelleReseau = reseauSociauxEntity.getLibReseau();
    }
}
