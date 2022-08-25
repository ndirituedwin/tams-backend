package com.cardgame.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "card_market_place_logs")
public class CardMarketplacelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String previousstatus;
    private String newstatus;
    private Long uid;
    private Long cardid;
    private Instant  createdDate;
    public CardMarketplacelog() {
    }


    public CardMarketplacelog(Long id, String action, String previousstatus, String newstatus, Long uid, Long cardid, Instant createdDate) {
        this.id = id;
        this.action = action;
        this.previousstatus = previousstatus;
        this.newstatus = newstatus;
        this.uid = uid;
        this.cardid = cardid;
        this.createdDate = createdDate;
    }

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPreviousstatus() {
        return previousstatus;
    }

    public void setPreviousstatus(String previousstatus) {
        this.previousstatus = previousstatus;
    }

    public String getNewstatus() {
        return newstatus;
    }

    public void setNewstatus(String newstatus) {
        this.newstatus = newstatus;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }



    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
