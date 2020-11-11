package com.silexsecure.arusdriver.service;

import java.util.List;

public class ErrorMessage {

    private Object user;
    private List<String> errors = null;
    private Object username;

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }
}
