package com.cardgame.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "game_winners_logs")
public class Gamewinner {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_uid")
    private Long playeruid;

    @Column(name = "gameroomtable_uid")
    private Long gameRoomTableuid;

    private String action;

    private String cards;

    @Column(name = "winninghand_indexes")
    private String indexes;

    @Column(name = "amount_won")
    private BigDecimal amount;

    @Column(name = "won_at")
    private Instant createddate;

    public Gamewinner() {
    }

    public Gamewinner(Long id, Long playeruid, Long gameRoomTableuid, String action, String cards, String indexes, BigDecimal amount, Instant createddate) {
        this.id = id;
        this.playeruid = playeruid;
        this.gameRoomTableuid = gameRoomTableuid;
        this.action = action;
        this.cards = cards;
        this.indexes = indexes;
        this.amount = amount;
        this.createddate = createddate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayeruid() {
        return playeruid;
    }

    public void setPlayeruid(Long playeruid) {
        this.playeruid = playeruid;
    }

    public Long getGameRoomTableuid() {
        return gameRoomTableuid;
    }

    public void setGameRoomTableuid(Long gameRoomTableuid) {
        this.gameRoomTableuid = gameRoomTableuid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
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
