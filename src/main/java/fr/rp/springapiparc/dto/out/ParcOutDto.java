package fr.rp.springapiparc.dto.out;

import com.github.slugify.Slugify;
import fr.rp.springapiparc.entity.ParcEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParcOutDto {

    private int idParc;
    private String nomParc;
    private String slugParc;
    private String presentation;
    private String nomRegion;
    private Integer idRegion;
    private boolean parkingGratuit;
    private boolean restauration;
    private boolean boutique;
    private boolean sejour;
    private boolean transport;
    private String prixAdulte;
    private String prixEnfant;

    // Liste types
    // Date pour slideBar
    // Images

    public ParcOutDto(ParcEntity parcEntity) {
        this.idParc = parcEntity.getId();
        this.nomParc = parcEntity.getNomParc();

        final Slugify slg = Slugify.builder().build();
        this.slugParc = slg.slugify(parcEntity.getNomParc());

        this.presentation = parcEntity.getPresentation().length() > 250 ? parcEntity.getPresentation().substring(0, 250) + "..." : parcEntity.getPresentation();
        this.nomRegion = parcEntity.getIdLieu().getIdRegion().getNomRegion();
        this.idRegion = parcEntity.getIdLieu().getIdRegion().getId();
        this.parkingGratuit = "Gratuit".equals(parcEntity.getIdParking().getParking());
        this.restauration = parcEntity.getRestauration();
        this.boutique = parcEntity.getBoutique();
        this.sejour = parcEntity.getSejour();
        this.transport = parcEntity.getAccesTransportCommun();
        this.prixAdulte = parcEntity.getPrixAdulte().toString();
        this.prixEnfant = parcEntity.getPrixEnfant().toString();
    }
}
