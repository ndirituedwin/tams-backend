package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Cardbyratioduplicaterequest {

    @NotBlank(message = "card id may not be blank")
    private String cardid;
    @NotBlank(message = "ratio")
    private String ratio;

    public Cardbyratioduplicaterequest() {
    }

    public Cardbyratioduplicaterequest(String cardid, String ratio) {
        this.cardid = cardid;
        this.ratio = ratio;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
