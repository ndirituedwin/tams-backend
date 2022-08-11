package com.cardgame.Dto.requests;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Addmoneytowalletrequest {


    private BigDecimal amount;
    @NotBlank(message = "payment id may not be blank")
    private String paymentid;
    @NotBlank(message = "order id may not be blank")
    private String orderid;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
