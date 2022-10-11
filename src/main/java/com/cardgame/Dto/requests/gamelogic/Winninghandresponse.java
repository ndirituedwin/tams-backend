package com.cardgame.Dto.requests.gamelogic;

public class Winninghandresponse {


private String message;
private String handindex;
private String player;
private Long uid;


    public Winninghandresponse(String message, String handindex, String player) {
        this.message = message;
        this.handindex = handindex;
        this.player = player;
    }

    public Winninghandresponse(String message, String handindex, String player, Long uid) {
        this.message = message;
        this.handindex = handindex;
        this.player = player;
        this.uid = uid;
    }

    public String getHandindex() {
        return handindex;
    }

    public void setHandindex(String handindex) {
        this.handindex = handindex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
