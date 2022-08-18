package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Getuserbuyinresponse {


    private long userid;
    private long gameroomid;
    private BigDecimal amount;
    private long id;
    private String message;
    public Getuserbuyinresponse() {
    }

    public Getuserbuyinresponse(String message) {
        this.message = message;
    }


    public Getuserbuyinresponse(long userid, long gameroomid, BigDecimal amount, long id) {
        this.userid = userid;
        this.gameroomid = gameroomid;
        this.amount = amount;
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getGameroomid() {
        return gameroomid;
    }

    public void setGameroomid(long gameroomid) {
        this.gameroomid = gameroomid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
