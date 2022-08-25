package com.cardgame.Dto.responses;


import com.cardgame.Entity.Gameroommaster;
import com.cardgame.Entity.Userwallet;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


public class GameRoomUsersResponse {

    private long id;

    private Gameroommaster gameroommaster;

    private Long uidone;


    private Long uidtwo;

    private Long uidthree;

    private Long uidfour;

    private Long uidfive;

    private int numberofusers;



    public GameRoomUsersResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gameroommaster getGameroommaster() {
        return gameroommaster;
    }

    public void setGameroommaster(Gameroommaster gameroommaster) {
        this.gameroommaster = gameroommaster;
    }

    public Long getUidone() {
        return uidone;
    }

    public void setUidone(Long uidone) {
        this.uidone = uidone;
    }

    public Long getUidtwo() {
        return uidtwo;
    }

    public void setUidtwo(Long uidtwo) {
        this.uidtwo = uidtwo;
    }

    public Long getUidthree() {
        return uidthree;
    }

    public void setUidthree(Long uidthree) {
        this.uidthree = uidthree;
    }

    public Long getUidfour() {
        return uidfour;
    }

    public void setUidfour(Long uidfour) {
        this.uidfour = uidfour;
    }

    public Long getUidfive() {
        return uidfive;
    }

    public void setUidfive(Long uidfive) {
        this.uidfive = uidfive;
    }

    public int getNumberofusers() {
        return numberofusers;
    }

    public void setNumberofusers(int numberofusers) {
        this.numberofusers = numberofusers;
    }
}
