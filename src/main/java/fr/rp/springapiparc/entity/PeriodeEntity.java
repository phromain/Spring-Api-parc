package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "periode")
public class PeriodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_date", nullable = false)
    private Integer id;

    @Column(name = "date_ouverture")
    private LocalDate dateOuverture;

    @Column(name = "date_fermeture")
    private LocalDate dateFermeture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_parc", nullable = false)
    private ParcEntity idParc;

}