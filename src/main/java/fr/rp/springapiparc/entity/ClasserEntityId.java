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
public class ClasserEntityId implements Serializable {
    @Serial
    private static final long serialVersionUID = -943159996633119874L;
    @Column(name = "id_type", nullable = false)
    private Integer idType;

    @Column(name = "id_parc", nullable = false)
    private Integer idParc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClasserEntityId entity = (ClasserEntityId) o;
        return Objects.equals(this.idType, entity.idType) &&
                Objects.equals(this.idParc, entity.idParc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idType, idParc);
    }

}