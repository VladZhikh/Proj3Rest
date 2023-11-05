package sensorTracking.project3.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sensorTracking.project3.models.Measurement;
import sensorTracking.project3.models.Sensor;
import sensorTracking.project3.repositories.SensorRepository;
import sensorTracking.project3.util.SensorNotFound;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }
    @Transactional
    public void newSensor(Sensor sensor){
        sensorRepository.save(sensor);
    }
    public Optional<Sensor> findByName(String name){
        return sensorRepository.findByName(name);
        //return foundSensor.orElseThrow(SensorNotFound::new);
    }
}
