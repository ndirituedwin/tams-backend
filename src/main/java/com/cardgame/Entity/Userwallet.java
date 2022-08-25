package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;


@Table(name = "user_wallet")
@Entity
public class Userwallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_id",unique = true)
    private Long userid;


    @Column(name = "total_wallet_balance")
    private BigDecimal totalwalletbalance;

    public Userwallet() {
    }

    public Userwallet(Long id, Long userid, BigDecimal totalwalletbalance) {
        this.id = id;
        this.userid = userid;
        this.totalwalletbalance = totalwalletbalance;
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


    public BigDecimal getTotalwalletbalance() {
        return totalwalletbalance;
    }

    public void setTotalwalletbalance(BigDecimal totalwalletbalance) {
        this.totalwalletbalance = totalwalletbalance;
    }
}
