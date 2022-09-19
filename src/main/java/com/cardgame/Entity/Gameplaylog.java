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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameRoomTable gameRoomTable;

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


    public Gameplaylog(Long id, User user, GameRoomTable gameRoomTable, String action, BigDecimal amount, Integer second, Instant createddate) {
        this.id = id;
        this.user = user;
        this.gameRoomTable = gameRoomTable;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameRoomTable getGameRoomTable() {
        return gameRoomTable;
    }

    public void setGameRoomTable(GameRoomTable gameRoomTable) {
        this.gameRoomTable = gameRoomTable;
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
