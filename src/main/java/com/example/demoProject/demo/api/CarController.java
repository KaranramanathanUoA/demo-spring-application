package com.example.demoProject.demo.api;

import com.example.demoProject.demo.model.Car;
import com.example.demoProject.demo.service.CarService;
import com.example.demoProject.demo.utils.CarNotFoundException;
import com.example.demoProject.demo.utils.CarRecordAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping("/cars")
    public List<Car> getAllCars(){
        return carService.getAllCars();
    }

    @RequestMapping("/cars/{vehicleId}")
    public Car getCar(@PathVariable Integer vehicleId) throws CarNotFoundException {
        return carService.getCar(vehicleId);
    }

    @PostMapping("/cars")
    public Car addCar(@Valid @RequestBody Car addCar) throws CarRecordAlreadyExistsException {
        return carService.addCar(addCar);
    }
}
