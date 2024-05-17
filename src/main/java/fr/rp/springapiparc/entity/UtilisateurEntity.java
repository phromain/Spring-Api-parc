package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "utilisateur")
public class UtilisateurEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur", nullable = false)
    private Integer id;

    @Column(name = "pseudo", nullable = false, length = 50)
    private String pseudo;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "mdp", nullable = false, length = 250)
    private String mdp;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "etat", nullable = false)
    private Boolean etat = false;

    @Column(name = "token", length = 250)
    private String token;

}