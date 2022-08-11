package com.cardgame.Dto.responses;

import com.cardgame.Entity.User;
import com.cardgame.Entity.UserCard;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.Instant;

public class CardFeeResponse {

    private Long id;

    private Long userCard;

    private Long user;

    private BigDecimal feeamount;

    public CardFeeResponse() {
    }

    public CardFeeResponse(Long id, Long userCard, Long user, BigDecimal feeamount) {
        this.id = id;
        this.userCard = userCard;
        this.user = user;
        this.feeamount = feeamount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserCard() {
        return userCard;
    }

    public void setUserCard(Long userCard) {
        this.userCard = userCard;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public BigDecimal getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(BigDecimal feeamount) {
        this.feeamount = feeamount;
    }
}
