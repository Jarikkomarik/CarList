package com.example.carlist.dto;

import java.util.Objects;

public class Car {

    private String plate;
    private String make;
    private String model;
    private String doorAmount;
    private String maxSpeed;
    private String price;

    public Car(String plate, String make, String model, String doorAmount, String maxSpeed, String price) {
        this.plate = plate;
        this.make = make;
        this.model = model;
        this.doorAmount = doorAmount;
        this.maxSpeed = maxSpeed;
        this.price = price;
    }

    public Car(String plate) {
        this.plate = plate;
    }

    public String getPlate() {
        return plate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getDoorAmount() {
        return doorAmount;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(plate, car.plate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate);
    }

    @Override
    public String toString() {
        return "<p>" +
                "License Plate ='" + plate + '\'' +
                ", Make='" + make + '\'' +
                ", Model='" + model + '\'' +
                ", DoorAmount='" + doorAmount + '\'' +
                ", MaxSpeed='" + maxSpeed + '\'' + " km/h" +
                ", Price='" + price + '\'' + " usd" +
                "</p>";
    }
}
