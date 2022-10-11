package com.cardgame.Dto.requests;

import javax.validation.constraints.NotNull;

public class Getuserbestcardrequest {

    @NotNull(message = "uid may not be null")
    private Long uid;

    public Getuserbestcardrequest() {
    }

    public Getuserbestcardrequest(Long uid) {
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
