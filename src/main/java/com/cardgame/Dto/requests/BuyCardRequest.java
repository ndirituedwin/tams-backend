package com.cardgame.Dto.requests;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class BuyCardRequest {


    @NotBlank(message = "card id may not be blank")
    private String cardid;
    private BigDecimal amounttobuy;
    @NotBlank(message = "payment id may not be blank")
    private String paymentid;
    @NotBlank(message = "order id may not be blank")
    private String orderid;


    public BuyCardRequest(String cardid, BigDecimal amounttobuy, String paymentid, String orderid) {
        this.cardid = cardid;
        this.amounttobuy = amounttobuy;
        this.paymentid = paymentid;
        this.orderid = orderid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public BigDecimal getAmounttobuy() {
        return amounttobuy;
    }

    public void setAmounttobuy(BigDecimal amounttobuy) {
        this.amounttobuy = amounttobuy;
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
