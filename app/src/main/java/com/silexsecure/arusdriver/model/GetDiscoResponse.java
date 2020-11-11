
package com.silexsecure.arusdriver.model;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class GetDiscoResponse {

    @Expose
    private List<Disco> disco;
    @Expose
    private String message;
    @Expose
    private Long status;

    public List<Disco> getDisco() {
        return disco;
    }

    public void setDisco(List<Disco> disco) {
        this.disco = disco;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
