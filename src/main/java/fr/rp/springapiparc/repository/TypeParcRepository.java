package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.TypeParcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeParcRepository extends JpaRepository<TypeParcEntity, Integer> {
}
