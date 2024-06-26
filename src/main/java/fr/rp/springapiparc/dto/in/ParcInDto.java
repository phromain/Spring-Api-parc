package fr.rp.springapiparc.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ParcInDto {

    @Size(max = 50)
    @NotBlank(message = "Le nom du parc ne peut pas être vide")
    @Pattern(regexp = "^[\\p{L} -]+$", message = "Seules les lettres, les espaces et les tirets sont autorisés")
    @JsonProperty("nomParc")
    private String nomParc;

    @NotBlank(message = "La présentation ne peut pas être vide")
    @JsonProperty("presentation")
    private String presentation;

    @Size(max = 228)
    @NotBlank(message = "L'adresse ne peut pas être vide")
    @JsonProperty("adresse")
    private String adresse;

    @NotNull(message = "La longitude du parc ne peut pas être nulle")
    @Digits(integer = 2, fraction = 6, message = "La précision maximale est de 2 chiffres entiers et 6 chiffres après la virgule")
    @JsonProperty("longitudeParc")
    private BigDecimal longitudeParc;

    @NotNull(message = "La latitude du parc ne peut pas être nulle")
    @Digits(integer = 3, fraction = 6, message = "La précision maximale est de 3 chiffres entiers et 6 chiffres après la virgule")
    @JsonProperty("lattitudeParc")
    private BigDecimal lattitudeParc;


    @NotBlank(message = "Le site internet ne peut pas être vide")
    @Size(max = 250)
    @JsonProperty("siteInternet")
    private String siteInternet;

    @NotNull(message = "Le numéro de téléphone du parc ne peut pas être nul")
    @JsonProperty("numeroTelParc")
    private Integer numeroTelParc;

    @NotNull(message = "Le prix adulte ne peut pas être nul")
    @Digits(integer = 3, fraction = 2, message = "La précision maximale est de 3 chiffres entiers et 2 chiffres après la virgule")
    @JsonProperty("prixAdulte")
    private BigDecimal prixAdulte;

    @NotNull(message = "Le prix enfant ne peut pas être nul")
    @Digits(integer = 3, fraction = 2, message = "La précision maximale est de 3 chiffres entiers et 2 chiffres après la virgule")
    @JsonProperty("prixEnfant")
    private BigDecimal prixEnfant;

    @Size(max = 250)
    @JsonProperty("urlAffiliation")
    private String urlAffiliation;

    @NotNull(message = "Le champ restauration ne peut pas être nul")
    @JsonProperty("restauration")
    private Boolean restauration;

    @NotNull(message = "Le champ boutique ne peut pas être nul")
    @JsonProperty("boutique")
    private Boolean boutique;

    @NotNull(message = "Le champ séjour ne peut pas être nul")
    @JsonProperty("sejour")
    private Boolean sejour;

    @NotNull(message = "Le champ accès transport commun ne peut pas être nul")
    @JsonProperty("accesTransportCommun")
    private Boolean accesTransportCommun;
}
