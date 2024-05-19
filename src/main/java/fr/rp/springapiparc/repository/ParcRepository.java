package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.ParcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcRepository extends JpaRepository<ParcEntity, Integer> {
}
