package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Withdrawresponse {


    private String message;
    private BigDecimal amountavailable;

    public Withdrawresponse(String message, BigDecimal amountavailable) {
        this.message = message;
        this.amountavailable = amountavailable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getAmount() {
        return amountavailable;
    }

    public void setAmount(BigDecimal amountavailable) {
        this.amountavailable = amountavailable;
    }
}
