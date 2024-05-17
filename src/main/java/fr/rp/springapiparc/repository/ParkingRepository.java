package repository;

import entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository  extends JpaRepository<ParkingEntity, Integer> {
}
