
package com.silexsecure.arusdriver.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class RegisterResponse {

    @Expose
    private String message;
    @Expose
    private Long otp;
    @Expose
    private Long userid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

}
