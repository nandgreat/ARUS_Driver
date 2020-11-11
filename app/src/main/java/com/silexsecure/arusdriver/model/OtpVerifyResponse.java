
package com.silexsecure.arusdriver.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class OtpVerifyResponse {

    @Expose
    private String message;
    @Expose
    private User user;
    @Expose
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
