package com.silexsecure.arusdriver.model;

public class ProductSale {

    private String id;
    private String customer;
    private String driver;
    private String product;
    private Double pricePerLitre;
    private String customerName;
    private String customerPhone;
    private String driverName;
    private String driverPhone;
    private Double pricePerKg;
    private Double quantityInKg;
    private Double quantityInLitres;
    private String requestKey;
    private Double amount;
    private String paymentType;
    private String address;
    private String latitude;
    private String longitude;
    private String paymentStatus;
    private String deliveryStatus;
    private String orderTime;

    public ProductSale() {
    }

    public ProductSale(String id, String customer, String driver, String product, Double pricePerLitre, String driverName, String driverPhone,
                       Double pricePerKg, Double quantityInKg, Double quantityInLitres, Double amount, String customerName, String requestKey, String customerPhone,
                       String paymentType, String address, String latitude, String longitude, String paymentStatus,
                       String deliveryStatus, String orderTime) {
        this.id = id;
        this.customer = customer;
        this.driver = driver;
        this.product = product;
        this.pricePerLitre = pricePerLitre;
        this.pricePerKg = pricePerKg;
        this.quantityInKg = quantityInKg;
        this.quantityInLitres = quantityInLitres;
        this.amount = amount;
        this.paymentType = paymentType;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
        this.orderTime = orderTime;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.requestKey = requestKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getPricePerLitre() {
        return pricePerLitre;
    }

    public void setPricePerLitre(Double pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    public Double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(Double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public Double getQuantityInKg() {
        return quantityInKg;
    }

    public void setQuantityInKg(Double quantityInKg) {
        this.quantityInKg = quantityInKg;
    }

    public Double getQuantityInLitres() {
        return quantityInLitres;
    }

    public void setQuantityInLitres(Double quantityInLitres) {
        this.quantityInLitres = quantityInLitres;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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
