package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Selectpackrequest {


    @NotBlank(message = "select pack")
    private String packid;
    private Integer openpack;
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPackid() {
        return packid;
    }

    public void setPackid(String packid) {
        this.packid = packid;
    }

    public Integer getOpenpack() {
        return openpack;
    }

    public void setOpenpack(Integer openpack) {
        this.openpack = openpack;
    }
}
