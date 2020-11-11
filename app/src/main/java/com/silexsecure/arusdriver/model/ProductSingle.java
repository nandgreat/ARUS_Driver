package com.silexsecure.arusdriver.model;

public class ProductSingle {
    private String name;
    private int thumbnail;
    private String make;
    private int amount;

    public ProductSingle() {
    }

    public ProductSingle(String name, String make, int amount, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.make = make;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}