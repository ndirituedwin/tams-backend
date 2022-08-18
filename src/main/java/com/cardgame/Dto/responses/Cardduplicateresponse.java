package com.cardgame.Dto.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cardduplicateresponse {

private String message;
private long cardid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCardid() {
        return cardid;
    }

    public void setCardid(long cardid) {
        this.cardid = cardid;
    }

    public Cardduplicateresponse() {
    }

    public Cardduplicateresponse(String message) {
        this.message = message;
    }

    public Cardduplicateresponse(String message, long cardid) {
        this.message = message;
        this.cardid = cardid;
    }
}
