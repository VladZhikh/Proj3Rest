package sensorTracking.project3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sensorTracking.project3.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {
}
