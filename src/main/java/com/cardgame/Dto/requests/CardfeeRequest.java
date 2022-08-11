package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class CardfeeRequest {

    private Long cardid;
    private BigDecimal cardfee;

    public CardfeeRequest(Long cardid, BigDecimal cardfee) {
        this.cardid = cardid;
        this.cardfee = cardfee;
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
