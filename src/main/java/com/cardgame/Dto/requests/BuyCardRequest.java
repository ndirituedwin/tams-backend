package com.cardgame.Dto.requests;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class BuyCardRequest {


    @NotBlank(message = "card id may not be blank")
    private String cardid;
    @NotBlank(message = "payment id may not be blank")
    private String paymentid;
    @NotBlank(message = "order id may not be blank")
    private String orderid;

     private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }



    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }


    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
