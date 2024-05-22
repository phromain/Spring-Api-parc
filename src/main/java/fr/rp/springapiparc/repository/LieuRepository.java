package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.LieuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LieuRepository extends JpaRepository<LieuEntity, Integer> {
}
