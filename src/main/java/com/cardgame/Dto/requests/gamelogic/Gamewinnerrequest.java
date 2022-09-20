package com.cardgame.Dto.requests.gamelogic;

import java.math.BigDecimal;

public class Gamewinnerrequest{

    private BigDecimal amount;
    private Long gameroomtableid;

    public Gamewinnerrequest() {
    }

    public Gamewinnerrequest(BigDecimal amount, Long gameroomtableid) {
        this.amount = amount;
        this.gameroomtableid = gameroomtableid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGameroomtableid() {
        return gameroomtableid;
    }

    public void setGameroomtableid(Long gameroomtableid) {
        this.gameroomtableid = gameroomtableid;
    }
}