
package com.silexsecure.arusdriver.model;

import com.google.gson.annotations.Expose;


@SuppressWarnings("unused")
public class LoginResponse {

    @Expose
    private String message;
    @Expose
    private String token;
    @Expose
    private User user;
    @Expose
    private int wallet;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
