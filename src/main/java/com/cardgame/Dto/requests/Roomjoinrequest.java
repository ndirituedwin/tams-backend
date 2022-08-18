package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Roomjoinrequest {


//    @NotBlank(message = "minimum amount may not be blank")
    private String minimumamount;
    private Long userid;
    private BigDecimal amount;
    private Long gameroomid;


    public String getMinimumamount() {
        return minimumamount;
    }

    public void setMinimumamount(String minimumamount) {
        this.minimumamount = minimumamount;
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
