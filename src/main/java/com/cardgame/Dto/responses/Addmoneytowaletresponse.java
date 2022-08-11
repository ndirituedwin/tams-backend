package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Addmoneytowaletresponse {



    private String message;
    private BigDecimal amount;
    private String orderid;
    private String paymentid;



    public Addmoneytowaletresponse(String message) {
        this.message = message;
    }


    public Addmoneytowaletresponse(String message, BigDecimal amount) {
        this.message = message;
        this.amount = amount;
    }

    public Addmoneytowaletresponse(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }
}
