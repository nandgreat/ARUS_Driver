package com.silexsecure.arusdriver.model;

public class OrderHistory {

    String customer;
    String driver;
    String product;
    String pricePerLitre;
    String pricePerKg;
    String quantityInKg;
    String quantityInLitres;
    String amount;
    String paymentType;
    String address;
    String latitude;
    String longitude;
    String paymentStatus;
    String deliveryStatus;
    String orderTime;

    public OrderHistory() {
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPricePerLitre() {
        return pricePerLitre;
    }

    public void setPricePerLitre(String pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    public String getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(String pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public String getQuantityInKg() {
        return quantityInKg;
    }

    public void setQuantityInKg(String quantityInKg) {
        this.quantityInKg = quantityInKg;
    }

    public String getQuantityInLitres() {
        return quantityInLitres;
    }

    public void setQuantityInLitres(String quantityInLitres) {
        this.quantityInLitres = quantityInLitres;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
