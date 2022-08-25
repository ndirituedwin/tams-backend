package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class PackfeeRequest {

    private Long packid;
    private Long uid;
    private BigDecimal packfee;

    public PackfeeRequest() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public PackfeeRequest(Long packid, Long uid, BigDecimal packfee) {
        this.packid = packid;
        this.uid = uid;
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
