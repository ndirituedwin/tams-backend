package com.cardgame.Dto.requests.gamelogic;

public class Winninghandresponse {


private String message;
private String player;

    public Winninghandresponse(String message, String player) {
        this.message = message;
        this.player = player;
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
}
