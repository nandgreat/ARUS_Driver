package com.silexsecure.arusdriver.model;

public class CarModel {
    int carModelID;
    int carMakeID;
    String carModel;

    public CarModel() {
    }

    public CarModel(int carModelID, int carMakeID, String carModel) {
        this.carModelID = carModelID;
        this.carMakeID = carMakeID;
        this.carModel = carModel;
    }

    public int getCarModelID() {
        return carModelID;
    }

    public void setCarModelID(int carModelID) {
        this.carModelID = carModelID;
    }

    public int getCarMakeID() {
        return carMakeID;
    }

    public void setCarMakeID(int carMakeID) {
        this.carMakeID = carMakeID;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
