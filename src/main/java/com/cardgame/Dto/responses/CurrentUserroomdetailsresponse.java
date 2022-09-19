package com.cardgame.Dto.responses;

import com.cardgame.Entity.BuyIn;
import com.cardgame.Entity.Userbestcard;
import com.cardgame.Entity.Userwallet;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CurrentUserroomdetailsresponse {



    @JsonProperty
    private BuyIn buyIn;

    @JsonProperty
    private Userwallet userwallet;

    @JsonProperty
    private List<Userbestcard> userbestcardList;
    private String message;
    public CurrentUserroomdetailsresponse() {
    }

    public CurrentUserroomdetailsresponse(BuyIn buyIn, Userwallet userwallet, List<Userbestcard> userbestcardList, String message) {
        this.buyIn = buyIn;
        this.userwallet = userwallet;
        this.userbestcardList = userbestcardList;
        this.message = message;
    }

    public List<Userbestcard> getUserbestcardList() {
        return userbestcardList;
    }

    public void setUserbestcardList(List<Userbestcard> userbestcardList) {
        this.userbestcardList = userbestcardList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BuyIn getBuyIn() {
        return buyIn;
    }

    public void setBuyIn(BuyIn buyIn) {
        this.buyIn = buyIn;
    }

    public Userwallet getUserwallet() {
        return userwallet;
    }

    public void setUserwallet(Userwallet userwallet) {
        this.userwallet = userwallet;
    }
}
