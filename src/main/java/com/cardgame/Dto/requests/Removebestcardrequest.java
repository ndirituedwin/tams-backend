package com.cardgame.Dto.requests;

import javax.validation.constraints.NotNull;

public class Removebestcardrequest {

    @NotNull(message = "user id may not be null")
    private Long uid;
//    @NotNull(message = "the card may not be null")
    private Long usercardid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUsercardid() {
        return usercardid;
    }

    public void setUsercardid(Long usercardid) {
        this.usercardid = usercardid;
    }
}
