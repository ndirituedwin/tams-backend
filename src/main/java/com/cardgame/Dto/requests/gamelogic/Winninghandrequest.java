package com.cardgame.Dto.requests.gamelogic;

import com.cardgame.Entity.User;

public class Winninghandrequest {

    private long id;
    private User user;
    private long userbestcard;


    public Winninghandrequest() {
    }

    public Winninghandrequest(long id, User user, long userbestcard) {
        this.id = id;
        this.user = user;
        this.userbestcard = userbestcard;
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
}
