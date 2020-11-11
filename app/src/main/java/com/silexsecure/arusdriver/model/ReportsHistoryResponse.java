package com.silexsecure.arusdriver.model;

import java.io.Serializable;
import java.util.List;

public class ReportsHistoryResponse implements Serializable {
    private int status;
    private String message;
    private List<HistoryData> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(List<HistoryData> data) {
        this.data = data;
    }

    public List<HistoryData> getData() {
        return data;
    }
}