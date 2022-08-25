package com.cardgame.Dto.responses;

import com.cardgame.Entity.Unopenedpack;
import com.cardgame.Entity.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.Instant;

public class PackFeeResponse {



    private Long id;


    private Unopenedpack unopenedpack;


    private User user;



    private BigDecimal feeamount;




    public PackFeeResponse() {
    }

    public PackFeeResponse(Long id, Unopenedpack unopenedpack, User user, BigDecimal feeamount) {
        this.id = id;
        this.unopenedpack = unopenedpack;
        this.user = user;
        this.feeamount = feeamount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unopenedpack getUnopenedpack() {
        return unopenedpack;
    }

    public void setUnopenedpack(Unopenedpack unopenedpack) {
        this.unopenedpack = unopenedpack;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(BigDecimal feeamount) {
        this.feeamount = feeamount;
    }
}
