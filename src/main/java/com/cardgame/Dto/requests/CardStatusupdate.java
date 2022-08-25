package com.cardgame.Dto.requests;

public class CardStatusupdate {

    private Long cardid;
    private String status;
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public CardStatusupdate() {
    }

    public CardStatusupdate(Long cardid, String status) {
        this.cardid = cardid;
        this.status = status;
    }

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
