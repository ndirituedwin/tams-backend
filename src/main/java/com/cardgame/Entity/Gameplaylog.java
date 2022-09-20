package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "game_play_logs_table")
public class Gameplaylog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long player_uid;

    private Long gameRoomTableuid;

    @Column(name = "action")
    private String action;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "second_actionwas_performed")
    private Integer second;

    @Column(name = "time_theactionwas_performed")
    private Instant createddate;

    public Gameplaylog() {
    }


    public Gameplaylog(Long id, Long player_uid, Long gameRoomTableuid, String action, BigDecimal amount, Integer second, Instant createddate) {
        this.id = id;
        this.player_uid = player_uid;
        this.gameRoomTableuid = gameRoomTableuid;
        this.action = action;
        this.amount = amount;
        this.second = second;
        this.createddate = createddate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
    }
}
