package com.cardgame.Dto.responses;

import com.cardgame.Entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Cardsownedbycurrentplayerresponse {



    private Long uid;
    private String username;
    private UserCard userCard;

//    @JsonProperty
//    private Userwallet userwallet;
//    @JsonProperty
//    private List<UserCard> userCards=new ArrayList<>(0);

    public Cardsownedbycurrentplayerresponse() {
    }

    public Cardsownedbycurrentplayerresponse(Long uid, String username, UserCard userCard) {
        this.uid = uid;
        this.username = username;
        this.userCard = userCard;
    }
//    public Cardsownedbycurrentplayerresponse(Long uid, String username /**Userwallet userwallet,*/ /*List<UserCard> userCards*/) {
//        this.uid = uid;
//        this.username = username;
////        this.userwallet = userwallet;
////        this.userCards = userCards;
//    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserCard getUserCard() {
        return userCard;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }
    /**  public List<UserCard> getUserCards() {
        return userCards;
    }

    public void setUserCards(List<UserCard> userCards) {
        this.userCards = userCards;
    }

     public Userwallet getUserwallet() {
        return userwallet;
    }

    public void setUserwallet(Userwallet userwallet) {
        this.userwallet = userwallet;
    }
   @JsonProperty
    private List<BuyIn> buyIns=new ArrayList<>(0);

    @JsonProperty
    private List<Gameroomlog> gameroomlogs=new ArrayList<>(0);

    @JsonProperty
    private List<PackPricelisting> packPricelistings=new ArrayList<>(0);

    @JsonProperty
    private List<Unopenedpack> unopenedpacks=new ArrayList<>(0);

    @JsonProperty
    private List<Userbestcard> userbestcards=new ArrayList<>(0);


    @JsonProperty
    private List<Usercardfee> usercardfees=new ArrayList<>(0);

    */


}
