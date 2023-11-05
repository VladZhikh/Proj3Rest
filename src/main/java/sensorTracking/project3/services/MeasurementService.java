package sensorTracking.project3.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sensorTracking.project3.models.Measurement;
import sensorTracking.project3.repositories.MeasurementRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
    @Transactional
    public void newMeasurement(Measurement measurement){
        measurementRepository.save(measurement);
    }
    public List<Measurement> allMeasurement(){
        return measurementRepository.findAll();
    }
    public Measurement getOne(int id){
        Optional<Measurement> findMeasurement =measurementRepository.findById(id);
        return findMeasurement.orElse(null);
    }
}
