package fr.rp.springapiparc.dto.in;

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
    @NotNull(message = "Le nom du parc ne peut pas être nul")
    private String nomParc;

    @NotBlank(message = "La présentation ne peut pas être vide")
    @NotNull(message = "La présentation ne peut pas être nulle")
    private String presentation;

    @Size(max = 228)
    @NotBlank(message = "L'adresse ne peut pas être vide")
    @NotNull(message = "L'adresse ne peut pas être nulle")
    private String adresse;

    @NotBlank(message = "La longitude ne peut pas être vide")
    @NotNull(message = "La longitude du parc ne peut pas être nulle")
    @Digits(integer = 2, fraction = 6, message = "La précision maximale est de 2 chiffres entiers et 6 chiffres après la virgule")
    private BigDecimal longitudeParc;

    @NotBlank(message = "La latitude ne peut pas être vide")
    @NotNull(message = "La latitude du parc ne peut pas être nulle")
    @Digits(integer = 3, fraction = 6, message = "La précision maximale est de 3 chiffres entiers et 6 chiffres après la virgule")
    private BigDecimal lattitudeParc;


    @NotBlank(message = "Le site internet ne peut pas être vide")
    @Size(max = 250)
    @NotNull(message = "Le site internet ne peut pas être nul")
    private String siteInternet;

    @NotBlank(message = "Le numéro de téléphone du parc ne peut pas être vide")
    @NotNull(message = "Le numéro de téléphone du parc ne peut pas être nul")
    private Integer numeroTelParc;

    @NotBlank(message = "Le prix adulte du parc ne peut pas être vide")
    @NotNull(message = "Le prix adulte ne peut pas être nul")
    @Digits(integer = 3, fraction = 2, message = "La précision maximale est de 3 chiffres entiers et 2 chiffres après la virgule")
    private BigDecimal prixAdulte;

    @NotBlank(message = "Le prix enfant du parc ne peut pas être vide")
    @NotNull(message = "Le prix enfant ne peut pas être nul")
    @Digits(integer = 3, fraction = 2, message = "La précision maximale est de 3 chiffres entiers et 2 chiffres après la virgule")
    private BigDecimal prixEnfant;

    @Size(max = 250)
    private String urlAffiliation;

    @NotBlank(message = "Le champ restauration ne peut pas être vide")
    @NotNull(message = "Le champ restauration ne peut pas être nul")
    @AssertTrue(message = "La valeur de restauration doit être true ou false")
    private Boolean restauration = false;

    @NotBlank(message = "Le champ boutique ne peut pas être vide")
    @NotNull(message = "Le champ boutique ne peut pas être nul")
    @AssertTrue(message = "La valeur de boutique doit être true ou false")
    private Boolean boutique = false;

    @NotBlank(message = "Le champ séjour ne peut pas être vide")
    @NotNull(message = "Le champ séjour ne peut pas être nul")
    @AssertTrue(message = "La valeur de séjour doit être true ou false")
    private Boolean sejour = false;

    @NotBlank(message = "Le champ accès transport commun ne peut pas être vide")
    @NotNull(message = "Le champ accès transport commun ne peut pas être nul")
    @AssertTrue(message = "La valeur de accès transport commun doit être true ou false")
    private Boolean accesTransportCommun = false;

}
