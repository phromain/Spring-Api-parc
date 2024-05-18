package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.LieuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LieuRepository extends JpaRepository<LieuEntity, Integer> {
}
