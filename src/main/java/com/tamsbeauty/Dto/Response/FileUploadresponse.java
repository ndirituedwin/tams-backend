package com.tamsbeauty.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class FileUploadresponse {

    private String message;
    private boolean status;

    public FileUploadresponse() {
    }

    public FileUploadresponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
