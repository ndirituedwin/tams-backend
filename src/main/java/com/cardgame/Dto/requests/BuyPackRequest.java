package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class BuyPackRequest {

private Long packid;
private Long uid;
//private BigDecimal amount;
private String orderid;
private String paymentid;


    public BuyPackRequest(Long packid, Long uid, String orderid, String paymentid) {
        this.packid = packid;
        this.uid = uid;
        this.orderid = orderid;
        this.paymentid = paymentid;
    }

    public Long getPackid() {
        return packid;
    }

    public void setPackid(Long packid) {
        this.packid = packid;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
