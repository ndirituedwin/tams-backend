package com.cardgame.Dto.requests;

public class FindAllCardsByUserrequest {

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public FindAllCardsByUserrequest(Long uid) {
        this.uid = uid;
    }

    public FindAllCardsByUserrequest() {
    }
}
