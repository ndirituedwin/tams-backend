package com.cardgame.Dto.responses;

public class BuyPackresponse {

    private String message;

    public BuyPackresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
