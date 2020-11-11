package com.silexsecure.arusdriver.model;

public class Cart {

    int thumbnail;
    String name;
    double amount;
    int quantity;
    double pricePerOne;

    public Cart( String name, double amount, int thumbnail, int quantity, double pricePerOne) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
        this.pricePerOne = pricePerOne;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerOne() {
        return pricePerOne;
    }

    public void setPricePerOne(double pricePerOne) {
        this.pricePerOne = pricePerOne;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
