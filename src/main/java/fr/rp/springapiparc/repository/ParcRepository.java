package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.ParcEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParcRepository extends JpaRepository<ParcEntity, Integer> {
    Optional<ParcEntity> findBySlugParc(String slugParc);


}
