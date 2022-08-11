package com.cardgame.Dto.responses;


public class Buycardresponse {


    private String message;

    public Buycardresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
