package com.cardgame.Dto.requests;


public class BoughtpackStatusRequest {


    private String cardStatus;
    private Long cardid;
    private Long uid;

    public BoughtpackStatusRequest(String cardStatus, Long cardid) {
        this.cardStatus = cardStatus;
        this.cardid = cardid;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }
}
