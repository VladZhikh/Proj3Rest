package sensorTracking.project3.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sensorTracking.project3.DTO.MeasurementDTO;
import sensorTracking.project3.models.Measurement;
import sensorTracking.project3.models.Sensor;
import sensorTracking.project3.services.MeasurementService;
import sensorTracking.project3.services.SensorService;
import sensorTracking.project3.util.MeasurementNotCreatedException;
import sensorTracking.project3.util.ErrorResponse;
import sensorTracking.project3.util.MeasurementValidator;
import sensorTracking.project3.util.SensorNotFound;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;


    @Autowired
    public MeasurementController(MeasurementService measurementService,
                                 SensorService sensorService,
                                 MeasurementValidator measurementValidator,
                                 ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                      BindingResult bindingResult){
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurement.setCreatedAt(LocalDateTime.now());
        measurementValidator.validate(measurement,bindingResult);
        Sensor sensor=measurement.getSensor();
        String name =sensor.getName();
        Sensor findedSensor = sensorService.findByName(name).orElseThrow(SensorNotFound::new);
        measurement.setSensor(findedSensor);
        //
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotCreatedException("Measurement not save, error- "+errorMsg.toString());
        }
        measurementService.newMeasurement(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotFound e){
        ErrorResponse response = new ErrorResponse("Sensor with this name was not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //Not Found 404
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //status - 400
    }

    @GetMapping()
    public List<MeasurementDTO> allMeasurements(){
        List<MeasurementDTO> measurementsDTO=measurementService.allMeasurement().
                stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
        return measurementsDTO;
    }

    @GetMapping("/rainyDaysCount")
    public String getRainyDaysCount(){
        int count=0;
        List<Measurement> measurements=measurementService.allMeasurement();
        for (Measurement measurement : measurements){
            if(measurement.isRaining()) ++count;
        }
        return "Count rainy days ="+count;
    }
    @GetMapping("/{id}")
    public MeasurementDTO getMeasurement(@PathVariable("id") int id){
        return convertToMeasurementDTO(measurementService.getOne(id));
    }
    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

}
