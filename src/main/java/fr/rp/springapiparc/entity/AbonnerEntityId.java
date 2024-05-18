package fr.rp.springapiparc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AbonnerEntityId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6250753842449042183L;
    @Column(name = "id_reseau_sociaux", nullable = false)
    private Integer idReseauSociaux;

    @Column(name = "id_parc", nullable = false)
    private Integer idParc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbonnerEntityId entity = (AbonnerEntityId) o;
        return Objects.equals(this.idReseauSociaux, entity.idReseauSociaux) &&
                Objects.equals(this.idParc, entity.idParc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReseauSociaux, idParc);
    }

}