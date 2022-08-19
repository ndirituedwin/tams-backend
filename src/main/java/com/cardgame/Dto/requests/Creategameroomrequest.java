package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class Creategameroomrequest {

    private BigDecimal minimumamount;
    private Long gameroomid;

    public Long getGameroomid() {
        return gameroomid;
    }

    public void setGameroomid(Long gameroomid) {
        this.gameroomid = gameroomid;
    }

    public BigDecimal getMinimumamount() {
        return minimumamount;
    }

    public void setMinimumamount(BigDecimal minimumamount) {
        this.minimumamount = minimumamount;
    }

}
