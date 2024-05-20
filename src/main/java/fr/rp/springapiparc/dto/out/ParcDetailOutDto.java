package fr.rp.springapiparc.dto.out;

import fr.rp.springapiparc.entity.ParcEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParcDetailOutDto {

    private String nomParc;
    private String presentation;
    private String nomRegion;
    private boolean restauration;
    private boolean boutique;
    private boolean sejour;
    private boolean transport;
    private String prixAdulte;
    private String prixEnfant;

    // Liste types
    // Date pour slideBar
    // Images
    // Reseaux Sociaux

    public ParcDetailOutDto(ParcEntity parcEntity) {
        this.nomParc = parcEntity.getNomParc();
        this.presentation = parcEntity.getPresentation();
        //this.libelleTypeParc = parcEntity.;
        this.nomRegion = parcEntity.getIdLieu().getIdRegion().getNomRegion();
        //this.urlImgPrez = urlImgPrez;
        this.restauration = parcEntity.getRestauration();
        this.boutique = parcEntity.getBoutique();
        this.sejour = parcEntity.getSejour();
        this.transport = parcEntity.getAccesTransportCommun();
        this.prixAdulte = parcEntity.getPrixAdulte().toString();
        this.prixEnfant = parcEntity.getPrixEnfant().toString();
    }
}
