package com.silexsecure.arusdriver.model;

import java.io.Serializable;

public class Data implements Serializable {
    private String consumerID;
    private String Username;
    private String Password;
    private String MobileNumber;
    private String OperatorID;
    private String id;
    private String OperatorName;
    private String records;

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public void setConsumerID(String consumerID) {
        this.consumerID = consumerID;
    }

    public String getConsumerID() {
        return consumerID;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getUsername() {
        return Username;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getPassword() {
        return Password;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setOperatorID(String operatorID) {
        this.OperatorID = OperatorID;
    }

    public String getOperatorID() {
        return OperatorID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOperatorName(String OperatorName) {
        this.OperatorName = OperatorName;
    }

    public String getOperatorName() {
        return OperatorName;
    }
}