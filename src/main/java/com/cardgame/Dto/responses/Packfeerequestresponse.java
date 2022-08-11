package com.cardgame.Dto.responses;



        import java.math.BigDecimal;

public class Packfeerequestresponse {




    private String message;

    public Packfeerequestresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
