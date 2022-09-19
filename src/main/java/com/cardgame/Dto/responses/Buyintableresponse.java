package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Buyintableresponse {


    private Long buyinid;
    private BigDecimal amount;
    private Long gameroomtable;
    private Long gameroommasterid;
    private Long uid;

    public Buyintableresponse() {
    }

    public Buyintableresponse(Long buyinid, BigDecimal amount, Long gameroomtable, Long gameroommasterid, Long uid) {
        this.buyinid = buyinid;
        this.amount = amount;
        this.gameroomtable = gameroomtable;
        this.gameroommasterid = gameroommasterid;
        this.uid = uid;
    }

    public Long getBuyinid() {
        return buyinid;
    }

    public void setBuyinid(Long buyinid) {
        this.buyinid = buyinid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGameroomtable() {
        return gameroomtable;
    }

    public void setGameroomtable(Long gameroomtable) {
        this.gameroomtable = gameroomtable;
    }

    public Long getGameroommasterid() {
        return gameroommasterid;
    }

    public void setGameroommasterid(Long gameroommasterid) {
        this.gameroommasterid = gameroommasterid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
