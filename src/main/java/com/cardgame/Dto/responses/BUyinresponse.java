package com.cardgame.Dto.responses;

public class BUyinresponse {

    private String message;
    private String status;

    public BUyinresponse() {
    }

    public BUyinresponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
