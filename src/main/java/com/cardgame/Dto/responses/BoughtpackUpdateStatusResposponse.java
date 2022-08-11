package com.cardgame.Dto.responses;

public class BoughtpackUpdateStatusResposponse {

private String  message;

    public BoughtpackUpdateStatusResposponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
