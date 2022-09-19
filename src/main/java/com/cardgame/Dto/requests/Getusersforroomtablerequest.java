package com.cardgame.Dto.requests;

public class Getusersforroomtablerequest {

    private Long uid;
    private Long roomid;

    public Getusersforroomtablerequest() {
    }

    public Getusersforroomtablerequest(Long uid, Long roomid) {
        this.uid = uid;
        this.roomid = roomid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }
}
