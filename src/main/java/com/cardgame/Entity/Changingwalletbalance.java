package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "changing_wallet_balances")
public class Changingwalletbalance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long userid;
    private String action;
    @Column(name = "previous_balance")
    private BigDecimal previousbalance;
    @Column(name = "new_balance")
    private BigDecimal newbalance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Userwallet userwallet;

    public Changingwalletbalance() {
    }

    public Changingwalletbalance(Long id, Long userid, String action, BigDecimal previousbalance, BigDecimal newbalance, Userwallet userwallet) {
        this.id = id;
        this.userid = userid;
        this.action = action;
        this.previousbalance = previousbalance;
        this.newbalance = newbalance;
        this.userwallet = userwallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getPreviousbalance() {
        return previousbalance;
    }

    public void setPreviousbalance(BigDecimal previousbalance) {
        this.previousbalance = previousbalance;
    }

    public BigDecimal getNewbalance() {
        return newbalance;
    }

    public void setNewbalance(BigDecimal newbalance) {
        this.newbalance = newbalance;
    }

    public Userwallet getUserwallet() {
        return userwallet;
    }

    public void setUserwallet(Userwallet userwallet) {
        this.userwallet = userwallet;
    }
}
