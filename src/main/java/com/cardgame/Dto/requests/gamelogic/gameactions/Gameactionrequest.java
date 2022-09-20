package com.cardgame.Dto.requests.gamelogic.gameactions;

import java.math.BigDecimal;

public class Gameactionrequest {


    private Long player_uid;

    private Long gameRoomTableuid;

    private String action;

    private BigDecimal amount;

    private Integer second;

    public Gameactionrequest() {
    }

    public Gameactionrequest(Long player_uid, Long gameRoomTableuid, String action, BigDecimal amount, Integer second) {
        this.player_uid = player_uid;
        this.gameRoomTableuid = gameRoomTableuid;
        this.action = action;
        this.amount = amount;
        this.second = second;
    }

    public Long getPlayer_uid() {
        return player_uid;
    }

    public void setPlayer_uid(Long player_uid) {
        this.player_uid = player_uid;
    }

    public Long getGameRoomTableuid() {
        return gameRoomTableuid;
    }

    public void setGameRoomTableuid(Long gameRoomTableuid) {
        this.gameRoomTableuid = gameRoomTableuid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }


}
