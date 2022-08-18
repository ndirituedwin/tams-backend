package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class CardfeeRequest {

    private Long cardid;
    private BigDecimal cardfee;
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public CardfeeRequest(Long cardid, BigDecimal cardfee, Long uid) {
        this.cardid = cardid;
        this.cardfee = cardfee;
        this.uid = uid;
    }

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public BigDecimal getCardfee() {
        return cardfee;
    }

    public void setCardfee(BigDecimal cardfee) {
        this.cardfee = cardfee;
    }
}
