package fr.rp.springapiparc.dto.out;

import com.github.slugify.Slugify;
import fr.rp.springapiparc.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class ParcOutDto {

    private int idParc;
    private String nomParc;
    private String slugParc;
    private String presentation;
    private Set<String> typeParc;
    private Set<String> imagePrez;
    private Set<String> periodeOuverture;
    private String nomRegion;
    private Integer idRegion;
    private boolean parkingGratuit;
    private boolean restauration;
    private boolean boutique;
    private boolean sejour;
    private boolean transport;
    private String prixAdulte;
    private String prixEnfant;

    public ParcOutDto(ParcEntity parcEntity) {
        this.idParc = parcEntity.getId();
        this.nomParc = parcEntity.getNomParc();

        final Slugify slg = Slugify.builder().build();
        this.slugParc = slg.slugify(parcEntity.getNomParc());

        this.typeParc = new HashSet<>();
        for (TypeParcEntity typeParcEntity : parcEntity.getTypeParcs()) {
            this.typeParc.add(typeParcEntity.getLibelleTypeParc());
        }
        this.imagePrez = new HashSet<>();
        for (ImageEntity imageEntity : parcEntity.getImages()) {
            if (imageEntity.getIdAttributImg().getId() == 1) {
                this.imagePrez.add(imageEntity.getRefImg());
            }
        }

        this.periodeOuverture = new HashSet<>();
        for (PeriodeEntity periodeEntity : parcEntity.getPeriodes()) {
            this.periodeOuverture.add(periodeEntity.getDateOuverture().toString() + " - " + periodeEntity.getDateFermeture().toString());
        }


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
