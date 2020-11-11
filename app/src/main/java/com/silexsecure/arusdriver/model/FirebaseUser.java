package com.silexsecure.arusdriver.model;

public class FirebaseUser {

    private String username;
    private String phone;
    private boolean onlineStatus;

    public FirebaseUser(String username, String phone, boolean onlineStatus) {
        this.username = username;
        this.phone = phone;
        this.onlineStatus = onlineStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
