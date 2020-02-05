package com.example.demoProject.demo.service;

import com.example.demoProject.demo.model.Car;
import com.example.demoProject.demo.repository.CarRepository;
import com.example.demoProject.demo.utils.CarNotFoundException;
import com.example.demoProject.demo.utils.CarRecordAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        List <Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(cars::add);
        return cars;
    }

    public Car getCar(Integer vehicleId) throws CarNotFoundException {
        return carRepository.findById(vehicleId).orElseThrow(
                () -> new CarNotFoundException("Car with the provided vehicleId does not exist!")
        );
    }

    public Car addCar(Car addCar) throws CarRecordAlreadyExistsException {
        try {
            Car newCarId = getCar(addCar.getVehicleId());
        }

        catch (CarNotFoundException e) {
            return carRepository.save(addCar);
        }

        throw new CarRecordAlreadyExistsException("Car with vehicleId already exists in the database!");

    }
}
