package fr.rp.springapiparc.repository;

import fr.rp.springapiparc.entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository  extends JpaRepository<ParkingEntity, Integer> {
}
