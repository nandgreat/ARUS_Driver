
package com.silexsecure.arusdriver.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class OtpVerifyRequest {

    @Expose
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
