package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.TypeParcEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeParcRepository extends JpaRepository<TypeParcEntity, Integer> {
}
