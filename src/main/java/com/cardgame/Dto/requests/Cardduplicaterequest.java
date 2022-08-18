package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Cardduplicaterequest {

    @NotBlank(message = "card id may not be blank")
    private String cardid;
    @NotBlank(message = "number of replications may not be blank")
    private String numberofreplications;

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getNumberofreplications() {
        return numberofreplications;
    }

    public void setNumberofreplications(String numberofreplications) {
        this.numberofreplications = numberofreplications;
    }

    public Cardduplicaterequest(String cardid, String numberofreplications) {
        this.cardid = cardid;
        this.numberofreplications = numberofreplications;
    }

    public Cardduplicaterequest() {
    }
}
