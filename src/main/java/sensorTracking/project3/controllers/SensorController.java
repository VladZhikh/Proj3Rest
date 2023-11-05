package sensorTracking.project3.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sensorTracking.project3.DTO.SensorDTO;
import sensorTracking.project3.models.Sensor;
import sensorTracking.project3.services.SensorService;
import sensorTracking.project3.util.ErrorResponse;
import sensorTracking.project3.util.SensorNotCreatedException;
import sensorTracking.project3.util.SensorValidator;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    public SensorController(SensorService sensorService,
                            SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult){
        Sensor sensorToAdd = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToAdd,bindingResult);
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            //return errorMsg.toString();
            throw new SensorNotCreatedException("Sensor not save, error- "+errorMsg.toString());
        }
        sensorService.newSensor(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //status - 400
    }
    private Sensor convertToSensor(SensorDTO sensorDTO){
        //ModelMapper modelMapper = new ModelMapper();
        Sensor sensor = modelMapper.map(sensorDTO,Sensor.class);
//        Sensor sensor = new Sensor();
//        sensor.setName(sensorDTO.getName());
        return sensor;
    }

}
