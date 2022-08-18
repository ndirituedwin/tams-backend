package com.cardgame.Dto.requests;



public class UserBuyInRequest {


    private Long userid;
    private Long gameroomid;

    public UserBuyInRequest() {
    }

    public UserBuyInRequest(Long userid, Long gameroomid) {
        this.userid = userid;
        this.gameroomid = gameroomid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getGameroomid() {
        return gameroomid;
    }

    public void setGameroomid(Long gameroomid) {
        this.gameroomid = gameroomid;
    }

    class Buyinrequest{

    }
}
