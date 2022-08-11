package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Cardfeerequestresponse {




    private String message;

    public Cardfeerequestresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
