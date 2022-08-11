package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "bought_packs")
@Entity
public class Boughtpacktracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Unopenedpack unopenedpack;



    @Column(name = "new_user")
    private Long newuser;
    @Column(name = "bought_at")
    private BigDecimal amount;

    @Column(name = "created_at")
    private Instant createdat;

    private PackStatus packStatus;

    public Boughtpacktracking() {
    }

    public Boughtpacktracking(Long id, Unopenedpack unopenedpack, Long newuser, BigDecimal amount, Instant createdat, PackStatus packStatus) {
        this.id = id;
        this.unopenedpack = unopenedpack;
        this.newuser = newuser;
        this.amount = amount;
        this.createdat = createdat;
        this.packStatus = packStatus;
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



    public Long getNewuser() {
        return newuser;
    }

    public void setNewuser(Long newuser) {
        this.newuser = newuser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public PackStatus getPackStatus() {
        return packStatus;
    }

    public void setPackStatus(PackStatus packStatus) {
        this.packStatus = packStatus;
    }
}
