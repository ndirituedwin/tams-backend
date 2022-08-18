package com.cardgame.Dto.responses;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.Instant;

public class GameRoomResponse {


    private Long id;
    private Instant createddate;
    private BigDecimal minimumamount;
    private int numberofusers;

    public GameRoomResponse() {
    }

    public GameRoomResponse(Long id, Instant createddate, BigDecimal minimumamount, int numberofusers) {
        this.id = id;
        this.createddate = createddate;
        this.minimumamount = minimumamount;
        this.numberofusers = numberofusers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
    }

    public BigDecimal getMinimumamount() {
        return minimumamount;
    }

    public void setMinimumamount(BigDecimal minimumamount) {
        this.minimumamount = minimumamount;
    }

    public int getNumberofusers() {
        return numberofusers;
    }

    public void setNumberofusers(int numberofusers) {
        this.numberofusers = numberofusers;
    }
}
