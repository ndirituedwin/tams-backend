package com.cardgame.Dto.requests;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class JoinRoomRequest {

    @NotBlank(message = "minimum amount may not be blank")
    private String minimumamount;

    public JoinRoomRequest(String minimumamount) {
        this.minimumamount = minimumamount;
    }

    public String getMinimumamount() {
        return minimumamount;
    }

    public void setMinimumamount(String minimumamount) {
        this.minimumamount = minimumamount;
    }
}
