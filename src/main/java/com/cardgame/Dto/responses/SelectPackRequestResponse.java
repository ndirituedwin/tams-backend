package com.cardgame.Dto.responses;

import com.cardgame.Entity.Unopenedpack;
import com.cardgame.Entity.UserCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SelectPackRequestResponse {

private String message;
private List<UserCard> userCardList;

private Unopenedpack unopenedpack;

    public SelectPackRequestResponse(String message, List<UserCard> userCardList, Unopenedpack unopenedpack) {
        this.message = message;
        this.userCardList = userCardList;
        this.unopenedpack = unopenedpack;
    }

    public SelectPackRequestResponse(String message) {
        this.message = message;
    }

    public SelectPackRequestResponse(String message, List<UserCard> userCardList) {
        this.message = message;
        this.userCardList = userCardList;
    }

    public SelectPackRequestResponse() {
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

    public Unopenedpack getUnopenedpack() {
        return unopenedpack;
    }

    public void setUnopenedpack(Unopenedpack unopenedpack) {
        this.unopenedpack = unopenedpack;
    }
}
