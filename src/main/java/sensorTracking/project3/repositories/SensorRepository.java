package sensorTracking.project3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sensorTracking.project3.models.Sensor;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor,Integer> {
    Optional<Sensor> findByName(String name);
}
