package com.cardgame.Entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "game_rooms_master")
public class Gameroommaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer participants;

    private BigDecimal amount;

    private Instant createddate;

    @Column(name = "is_active")
    private boolean status;

    public Gameroommaster() {
    }

    public Gameroommaster(Integer participants, BigDecimal amount, Instant createddate, boolean status) {
        this.participants = participants;
        this.amount = amount;
        this.createddate = createddate;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
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
}
