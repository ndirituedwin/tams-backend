package com.cardgame.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


public class UnopenedPackRequestResponse {

private String message;

    public UnopenedPackRequestResponse() {
    }

    public UnopenedPackRequestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
