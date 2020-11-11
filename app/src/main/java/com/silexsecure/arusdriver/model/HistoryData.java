package com.silexsecure.arusdriver.model;

import java.io.Serializable;

public class HistoryData implements Serializable {
    private String ConsumersReportID;
    private String ConsumerID;
    private String OperatorID;
    private String ServiceTypeID;
    private String ClassofServiceID;
    private String RatingID;
    private String Area;
    private String Description;
    private String IMEI;
    private String StateID;
    private String Latitude;
    private String Longitude;
    private String FrequencyofOccurenceID;
    private String DateSubmitted;
    private String OperatorName;

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public String getServiceTypeName() {
        return ServiceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        ServiceTypeName = serviceTypeName;
    }

    private String ServiceTypeName;
    private String id;

    public void setConsumersReportID(String consumersReportID) {
        this.ConsumersReportID = consumersReportID;
    }

    public String getConsumersReportID() {
        return ConsumersReportID;
    }

    public void setConsumerID(String consumerID) {
        this.ConsumerID = consumerID;
    }

    public String getConsumerID() {
        return ConsumerID;
    }

    public void setOperatorID(String operatorID) {
        this.OperatorID = operatorID;
    }

    public String getOperatorID() {
        return OperatorID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.ServiceTypeID = serviceTypeID;
    }

    public String getServiceTypeID() {
        return ServiceTypeID;
    }

    public void setClassofServiceID(String classofServiceID) {
        this.ClassofServiceID = classofServiceID;
    }

    public String getClassofServiceID() {
        return ClassofServiceID;
    }

    public void setRatingID(String ratingID) {
        this.RatingID = ratingID;
    }

    public String getRatingID() {
        return RatingID;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public String getArea() {
        return Area;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setIMEI(String iMEI) {
        this.IMEI = iMEI;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setStateID(String stateID) {
        this.StateID = stateID;
    }

    public String getStateID() {
        return StateID;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setFrequencyofOccurenceID(String frequencyofOccurenceID) {
        this.FrequencyofOccurenceID = frequencyofOccurenceID;
    }

    public String getFrequencyofOccurenceID() {
        return FrequencyofOccurenceID;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.DateSubmitted = dateSubmitted;
    }

    public String getDateSubmitted() {
        return DateSubmitted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}