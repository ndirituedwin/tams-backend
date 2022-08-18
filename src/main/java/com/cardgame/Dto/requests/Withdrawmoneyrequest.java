package com.cardgame.Dto.requests;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Withdrawmoneyrequest {

    private BigDecimal withdrawamount;
    @NotBlank(message = "payment id may not be blank")
    private String paymentid;
    @NotBlank(message = "order id may not be blank")
    private String orderid;
    private Long uid;
    public Withdrawmoneyrequest() {
    }

    public Withdrawmoneyrequest(BigDecimal withdrawamount) {
        this.withdrawamount = withdrawamount;
    }

    public Withdrawmoneyrequest(Long uid) {
        this.uid = uid;
    }

    public BigDecimal getWithdrawamount() {
        return withdrawamount;
    }

    public void setWithdrawamount(BigDecimal withdrawamount) {
        this.withdrawamount = withdrawamount;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
