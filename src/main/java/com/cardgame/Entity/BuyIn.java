package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "buy_ins")
public class BuyIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
//            name = "UID",
            referencedColumnName = "UID"
    )
    private User user;

    private BigDecimal amount;

    private Instant createdDate;


    @ManyToOne(fetch = FetchType.LAZY)
    private Gameroommaster gameroommaster;

    @OneToOne(fetch = FetchType.LAZY)
    private GameRoomTable gameRoomTable;

    public BuyIn() {
    }

    public BuyIn(long id, User user, BigDecimal amount, Instant createdDate, Gameroommaster gameroommaster, GameRoomTable gameRoomTable) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.createdDate = createdDate;
        this.gameroommaster = gameroommaster;
        this.gameRoomTable = gameRoomTable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Gameroommaster getGameroommaster() {
        return gameroommaster;
    }

    public GameRoomTable getGameRoomTable() {
        return gameRoomTable;
    }

    public void setGameRoomTable(GameRoomTable gameRoomTable) {
        this.gameRoomTable = gameRoomTable;
    }

    public void setGameroommaster(Gameroommaster gameroommaster) {
        this.gameroommaster = gameroommaster;
    }
}
