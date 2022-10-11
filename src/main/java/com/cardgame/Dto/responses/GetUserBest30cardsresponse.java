package com.cardgame.Dto.responses;

import com.cardgame.Entity.Userbestcard;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetUserBest30cardsresponse {


    private Long uid;
    private String username;


    private Userbestcard userbestcard;

    public GetUserBest30cardsresponse() {
    }

    public GetUserBest30cardsresponse(Long uid, String username, Userbestcard userbestcard) {
        this.uid = uid;
        this.username = username;
        this.userbestcard = userbestcard;
    }

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

    public Userbestcard getUserbestcard() {
        return userbestcard;
    }

    public void setUserbestcard(Userbestcard userbestcard) {
        this.userbestcard = userbestcard;
    }
}
