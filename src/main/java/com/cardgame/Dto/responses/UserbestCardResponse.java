package com.cardgame.Dto.responses;


import lombok.Builder;
import lombok.Data;


public class UserbestCardResponse {

    private String message;

    public UserbestCardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}