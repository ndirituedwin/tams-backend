package com.cardgame.Dto.requests.gamelogic.gameactions;

public class Gameactionresponse {


    private String message;

    public Gameactionresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
