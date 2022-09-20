package com.cardgame.Dto.requests.gamelogic;

import com.cardgame.Entity.Gamewinner;
import com.cardgame.Entity.User;

import java.math.BigDecimal;

public class Winninghandrequest {

    private long id;
    private User user;
    private long userbestcard;

    private BigDecimal amount;
    private Long gameroomtableid;



    public Winninghandrequest() {
    }

    public Winninghandrequest(long id, User user, long userbestcard, BigDecimal amount, Long gameroomtableid) {
        this.id = id;
        this.user = user;
        this.userbestcard = userbestcard;
        this.amount = amount;
        this.gameroomtableid = gameroomtableid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUserbestcard() {
        return userbestcard;
    }

    public void setUserbestcard(long userbestcard) {
        this.userbestcard = userbestcard;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGameroomtableid() {
        return gameroomtableid;
    }

    public void setGameroomtableid(Long gameroomtableid) {
        this.gameroomtableid = gameroomtableid;
    }
}
