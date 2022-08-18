package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class Buyinrequest {


    private Long userid;
    private BigDecimal amount;
    private Long gameroomid;

    public Buyinrequest() {
    }

    public Buyinrequest(Long userid, BigDecimal amount, Long gameroomid) {
        this.userid = userid;
        this.amount = amount;
        this.gameroomid = gameroomid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGameroomid() {
        return gameroomid;
    }

    public void setGameroomid(Long gameroomid) {
        this.gameroomid = gameroomid;
    }
}
