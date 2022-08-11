package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class PackfeeRequest {

    private Long packid;
    private BigDecimal packfee;

    public PackfeeRequest() {
    }

    public PackfeeRequest(Long packid, BigDecimal packfee) {
        this.packid = packid;
        this.packfee = packfee;
    }

    public Long getPackid() {
        return packid;
    }

    public void setPackid(Long packid) {
        this.packid = packid;
    }

    public BigDecimal getPackfee() {
        return packfee;
    }

    public void setPackfee(BigDecimal packfee) {
        this.packfee = packfee;
    }
}
