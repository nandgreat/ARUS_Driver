package com.silexsecure.arusdriver.model;

import java.io.Serializable;

public class DataCustomer implements Serializable {
    private String Username;
    private String MobileNumber;
    private String password;
    private String id;
    private String OperatorName;
    private String records;

    public DataCustomer(String Username, String MobileNumber, String Password, String id, String OperatorName, String record) {
        this.Username = Username;
        this.MobileNumber = MobileNumber;
        this.password = Password;
        this.id = id;
        this.OperatorName = OperatorName;
        this.records = records;
    }

    public DataCustomer(String username, String mobileNumber, String id, String operatorName) {
        this.Username = username;
        this.MobileNumber = mobileNumber;
        this.id = id;
        this.OperatorName = operatorName;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setMobileNumber(String mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOperatorName(String operatorName) {
        this.OperatorName = operatorName;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }
}