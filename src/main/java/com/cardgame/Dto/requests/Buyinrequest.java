package com.cardgame.Dto.requests;

import java.math.BigDecimal;

public class Buyinrequest {


    private Long uid;
    private BigDecimal amount;
    private Long mastertableid;
    private Long roomid;

    public Buyinrequest() {
    }

    public Buyinrequest(Long uid, BigDecimal amount, Long mastertableid, Long roomid) {
        this.uid = uid;
        this.amount = amount;
        this.mastertableid = mastertableid;
        this.roomid = roomid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getMastertableid() {
        return mastertableid;
    }

    public void setMastertableid(Long mastertableid) {
        this.mastertableid = mastertableid;
    }

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }
}
