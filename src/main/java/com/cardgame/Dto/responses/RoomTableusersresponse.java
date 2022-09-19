package com.cardgame.Dto.responses;

import com.cardgame.Entity.BuyIn;
import com.cardgame.Entity.User;
import com.cardgame.Entity.Userbestcard;
import com.cardgame.Entity.Userwallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class RoomTableusersresponse {




    @JsonProperty
    public BuyIn buyIn;

    @JsonProperty
    public Userwallet userwallet;

    @JsonProperty
    private List<Userbestcard> userbestcardList;


    public RoomTableusersresponse(BuyIn buyIn, Userwallet userwallet, List<Userbestcard> userbestcardList) {
        this.buyIn = buyIn;
        this.userwallet = userwallet;
        this.userbestcardList = userbestcardList;
    }

//    public RoomTableusersresponse(BuyIn buyIn, Userwallet userwallet) {
//        this.buyIn = buyIn;
//        this.userwallet = userwallet;
//    }

    public RoomTableusersresponse() {
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

    public List<Userbestcard> getUserbestcardList() {
        return userbestcardList;
    }

    public void setUserbestcardList(List<Userbestcard> userbestcardList) {
        this.userbestcardList = userbestcardList;
    }
}
