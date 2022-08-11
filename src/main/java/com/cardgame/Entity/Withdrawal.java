package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userid;

    @Column(name = "amount_withdrawn")
    private BigDecimal amount;

    @Column(name = "created_at")
    private Instant createddate;
    @Column(name = "order_id")
    private String orderid;
    @Column(name = "payment_id")
    private String paymentid;


    public Withdrawal() {
    }

    public Withdrawal(Long id, Long userid, BigDecimal amount, Instant createddate, String orderid, String paymentid) {
        this.id = id;
        this.userid = userid;
        this.amount = amount;
        this.createddate = createddate;
        this.orderid = orderid;
        this.paymentid = paymentid;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
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
}
