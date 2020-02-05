package com.example.demoProject.demo.utils;

import java.util.function.Supplier;

public class CarNotFoundException extends Exception {

    public CarNotFoundException(String message){
        super(message);
    }
}
