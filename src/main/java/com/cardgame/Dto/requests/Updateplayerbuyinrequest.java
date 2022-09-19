package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class Updateplayerbuyinrequest {


    private BigDecimal buyin;

    private Long uid;
    private Long buyinid;
    private String status;

    public Long getBuyinid() {
        return buyinid;
    }

    public void setBuyinid(Long buyinid) {
        this.buyinid = buyinid;
    }

    public BigDecimal getBuyin() {
        return buyin;
    }

    public void setBuyin(BigDecimal buyin) {
        this.buyin = buyin;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
