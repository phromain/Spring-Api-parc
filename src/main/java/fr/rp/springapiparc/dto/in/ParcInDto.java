package fr.rp.springapiparc.dto.in;

import fr.rp.springapiparc.entity.LieuEntity;
import fr.rp.springapiparc.entity.ParkingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ParcInDto {

    @Column(name = "id_parc", nullable = false)
    private Integer id;

    @Column(name = "nom_parc", nullable = false, length = 50)
    private String nomParc;

    @Column(name = "presentation", nullable = false)
    private String presentation;

    @Column(name = "adresse", nullable = false, length = 228)
    private String adresse;

    @Column(name = "longitude_parc", nullable = false, precision = 8, scale = 6)
    private BigDecimal longitudeParc;

    @Column(name = "lattitude_parc", nullable = false, precision = 9, scale = 6)
    private BigDecimal lattitudeParc;

    @Column(name = "site_internet", nullable = false, length = 250)
    private String siteInternet;

    @Column(name = "numero_tel_parc", nullable = false)
    private Integer numeroTelParc;

    @Column(name = "prix_adulte", nullable = false, precision = 5, scale = 2)
    private BigDecimal prixAdulte;

    @Column(name = "prix_enfant", nullable = false, precision = 5, scale = 2)
    private BigDecimal prixEnfant;

    @Column(name = "url_affiliation", length = 250)
    private String urlAffiliation;

    @Column(name = "restauration", nullable = false)
    private Boolean restauration = false;

    @Column(name = "boutique", nullable = false)
    private Boolean boutique = false;

    @Column(name = "sejour", nullable = false)
    private Boolean sejour = false;

    @Column(name = "acces_transport_commun", nullable = false)
    private Boolean accesTransportCommun = false;

    private LieuEntity idLieu;
    private ParkingEntity idParking;


}
