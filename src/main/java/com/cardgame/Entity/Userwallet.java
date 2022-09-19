package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;


@Table(name = "user_wallet")
@Entity
public class Userwallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @Column(name = "user_id",unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;


    @Column(name = "total_wallet_balance")
    private BigDecimal totalwalletbalance;

    public Userwallet() {
    }

    public Userwallet(Long id, User user, BigDecimal totalwalletbalance) {
        this.id = id;
        this.user = user;
        this.totalwalletbalance = totalwalletbalance;
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

    public BigDecimal getTotalwalletbalance() {
        return totalwalletbalance;
    }

    public void setTotalwalletbalance(BigDecimal totalwalletbalance) {
        this.totalwalletbalance = totalwalletbalance;
    }
}
