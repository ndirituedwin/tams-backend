package com.cardgame.Entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pack_table_listing")
public class PackPricelisting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Unopenedpack unopenedpack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "UID")
    private User user;

    @Column(name = "created_at")
    private Instant createdat;

    @Column(name = "fee_amount")
    private BigDecimal feeamount;
    private String status;
    public PackPricelisting() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PackPricelisting(Long id, Unopenedpack unopenedpack, User user, Instant createdat, BigDecimal feeamount, String status) {
        this.id = id;
        this.unopenedpack = unopenedpack;
        this.user = user;
        this.createdat = createdat;
        this.feeamount = feeamount;
        this.status = status;
    }

    public Unopenedpack getUnopenedpack() {
        return unopenedpack;
    }

    public void setUnopenedpack(Unopenedpack unopenedpack) {
        this.unopenedpack = unopenedpack;
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

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public BigDecimal getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(BigDecimal feeamount) {
        this.feeamount = feeamount;
    }
}
