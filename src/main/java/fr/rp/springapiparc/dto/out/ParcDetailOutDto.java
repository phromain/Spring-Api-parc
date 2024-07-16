package fr.rp.springapiparc.dto.out;

import fr.rp.springapiparc.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class ParcDetailOutDto {

    private String nomParc;
    private String presentation;
    private Set<String> typeParc;
    private Set<Map<String, String>> images;
    private Set<String> periodeOuverture;
    private Map<String, String> reseauxSociaux;
    private String siteInternet;
    private String nomRegion;
    private String adresse;
    private String ville;
    private String codePostal;
    private boolean restauration;
    private boolean boutique;
    private boolean sejour;
    private boolean transport;
    private String parking;
    private String prixAdulte;
    private String prixEnfant;

    public ParcDetailOutDto(ParcEntity parcEntity) {
        this.nomParc = parcEntity.getNomParc();
        this.presentation = parcEntity.getPresentation();

        this.typeParc = new HashSet<>();
        for (TypeParcEntity typeParcEntity : parcEntity.getTypeParcs()) {
            this.typeParc.add(typeParcEntity.getLibelleTypeParc());
        }
        this.images = new HashSet<>();
        for (ImageEntity imageEntity : parcEntity.getImages()) {
            Map<String, String> imageMap = new HashMap<>();
            imageMap.put(imageEntity.getIdAttributImg().getLibAttributImg(), imageEntity.getRefImg());
            this.images.add(imageMap);
        }

        this.periodeOuverture = new HashSet<>();
        for (PeriodeEntity periodeEntity : parcEntity.getPeriodes()) {
            this.periodeOuverture.add(periodeEntity.getDateOuverture().toString() + " - " + periodeEntity.getDateFermeture().toString());
        }
        this.reseauxSociaux = new HashMap<>();
        for (AbonnerEntity abonnerEntity : parcEntity.getAbonners()) {
            ReseauSociauxEntity reseauSociauxEntity = abonnerEntity.getIdReseauSociaux();
            this.reseauxSociaux.put(reseauSociauxEntity.getLibReseau(), abonnerEntity.getUrl());
        }
        this.siteInternet = parcEntity.getSiteInternet();
        this.nomRegion = parcEntity.getIdLieu().getIdRegion().getNomRegion();
        this.adresse = parcEntity.getAdresse();
        this.ville = parcEntity.getIdLieu().getVille();
        this.codePostal = parcEntity.getIdLieu().getCodePostal();


        this.restauration = parcEntity.getRestauration();
        this.boutique = parcEntity.getBoutique();
        this.sejour = parcEntity.getSejour();
        this.transport = parcEntity.getAccesTransportCommun();
        this.parking = parcEntity.getIdParking().getParking();


        this.prixAdulte = parcEntity.getPrixAdulte().toString();
        this.prixEnfant = parcEntity.getPrixEnfant().toString();
    }
}
