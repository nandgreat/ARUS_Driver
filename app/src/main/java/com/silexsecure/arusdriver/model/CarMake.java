package com.silexsecure.arusdriver.model;

public class CarMake {
    int carMakeID;
    String carMake;

    public CarMake(int carMakeID, String carMake) {
        this.carMakeID = carMakeID;
        this.carMake = carMake;
    }

    public int getCarMakeID() {
        return carMakeID;
    }

    public void setCarMakeID(int carMakeID) {
        this.carMakeID = carMakeID;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }
}
