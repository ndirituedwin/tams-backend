package com.cardgame.Dto.requests;

public class Currentuserroomrequest {

    private Long roomid;
    private Long uid;

    public Currentuserroomrequest() {
    }

    public Currentuserroomrequest(Long roomid, Long uid) {
        this.roomid = roomid;
        this.uid = uid;
    }

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
