package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
}
