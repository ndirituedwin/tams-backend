package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

public class Userbestcardrequest {


@NotBlank(message = "best cards may not be blank")
private String userbestcards;

    public String getUserbestcards() {
        return userbestcards;
    }

    public void setUserbestcards(String userbestcards) {
        this.userbestcards = userbestcards;
    }
}
