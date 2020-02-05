package com.example.demoProject.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Car {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;

    @NotNull
    @NotEmpty(message = "'color' field was empty")
    private String color;

    @NotNull
    @NotEmpty(message = "'model' field was empty")
    private String model;

    public Car(String color, String model){
        this.color = color;
        this.model = model;
    }

    public Car(){}

    public String getModel() {
        return model;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public String getColor() {
        return color;
    }

    public Car(Integer vehicleId, String color, String model) {
        this.vehicleId = vehicleId;
        this.color = color;
        this.model = model;
    }
}
