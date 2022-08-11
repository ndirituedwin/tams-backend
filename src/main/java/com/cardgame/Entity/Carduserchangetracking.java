package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "user_changes_tracking")
public class Carduserchangetracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "original_user_id")
    private Long originaluser;

    @Column(name = "new_user_id")
    private Long newuser;

    @Column(name = "created_at")
    private Instant createdat;
    @Column(name = "amount_bought")
    private BigDecimal amountbought;

    public Carduserchangetracking() {
    }

    public Carduserchangetracking(Long id, Long originaluser, Long newuser, Instant createdat, BigDecimal amountbought) {
        this.id = id;
        this.originaluser = originaluser;
        this.newuser = newuser;
        this.createdat = createdat;
        this.amountbought = amountbought;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginaluser() {
        return originaluser;
    }

    public void setOriginaluser(Long originaluser) {
        this.originaluser = originaluser;
    }

    public Long getNewuser() {
        return newuser;
    }

    public void setNewuser(Long newuser) {
        this.newuser = newuser;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public BigDecimal getAmountbought() {
        return amountbought;
    }

    public void setAmountbought(BigDecimal amountbought) {
        this.amountbought = amountbought;
    }
}
