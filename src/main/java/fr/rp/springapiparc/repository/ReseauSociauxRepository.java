package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.ReseauSociauxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseauSociauxRepository extends JpaRepository<ReseauSociauxEntity, Integer> {
}
