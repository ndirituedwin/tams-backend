package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Unopenedpackrequest {


    @NotBlank(message = "select pack")
    private String packid;

    @NotNull(message = "open pack may no be null")
    private int openpack;

    public int getOpenpack() {
        return openpack;
    }

    public void setOpenpack(int openpack) {
        this.openpack = openpack;
    }

    public String getPackid() {
        return packid;
    }

    public void setPackid(String packid) {
        this.packid = packid;
    }
}
