package com.cardgame.Dto.responses;

public class Packstatusupdate {
    private Long packid;
    private String status;
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Packstatusupdate() {
    }

    public Packstatusupdate(Long packid, String status) {
        this.packid = packid;
        this.status = status;
    }


    public Long getPackid() {
        return packid;
    }

    public void setPackid(Long packid) {
        this.packid = packid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
