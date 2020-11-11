
package com.silexsecure.arusdriver.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class ChangePasswordResponse {

    @Expose
    private Driver driver;
    @Expose
    private String message;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
