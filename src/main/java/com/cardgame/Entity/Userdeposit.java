package com.cardgame.Entity;


import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "user_deposits_table")
@Entity
public class Userdeposit {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userid;

    @Column(name = "amount_added")
    private BigDecimal amountadded;
    @Column(name = "order_id")
    private String orderid;
    @Column(name = "payment_id")
    private String paymentid;


    @ManyToOne(fetch = FetchType.LAZY)
    private Userwallet userwallet;

    public Userdeposit() {
    }

    public Userdeposit(Long id, Long userid, BigDecimal amountadded, String orderid, String paymentid, Userwallet userwallet) {
        this.id = id;
        this.userid = userid;
        this.amountadded = amountadded;
        this.orderid = orderid;
        this.paymentid = paymentid;
        this.userwallet = userwallet;
    }

    public BigDecimal getAmountadded() {
        return amountadded;
    }

    public void setAmountadded(BigDecimal amountadded) {
        this.amountadded = amountadded;
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

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public Userwallet getUserwallet() {
        return userwallet;
    }

    public void setUserwallet(Userwallet userwallet) {
        this.userwallet = userwallet;
    }
}
