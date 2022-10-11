package com.cardgame.Dto.responses;

import com.cardgame.Entity.UserCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public class UnopenedPackRequestResponse {

private String message;
private List<UserCard> userCardList;


    public UnopenedPackRequestResponse(String message, List<UserCard> userCardList) {
        this.message = message;
        this.userCardList = userCardList;
    }

    public UnopenedPackRequestResponse() {
    }

    public UnopenedPackRequestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserCard> getUserCardList() {
        return userCardList;
    }

    public void setUserCardList(List<UserCard> userCardList) {
        this.userCardList = userCardList;
    }
}
