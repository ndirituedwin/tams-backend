package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

public class Roomjoinrequest {


    @NotBlank(message = "minimum amount may not be blank")
    private String minimumamount;

    public String getMinimumamount() {
        return minimumamount;
    }

    public void setMinimumamount(String minimumamount) {
        this.minimumamount = minimumamount;
    }
}
