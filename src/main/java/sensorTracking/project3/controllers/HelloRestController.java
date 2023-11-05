package sensorTracking.project3.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

    @GetMapping()
    public String helloRest(){
        return "Hello REST API";
    }
}
