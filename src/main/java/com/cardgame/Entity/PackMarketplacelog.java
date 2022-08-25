package com.cardgame.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pack_market_place_logs")
public class PackMarketplacelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String previousstatus;
    private String newstatus;
    private Long uid;
    private Long packid;
    private Instant  createdDate;

    public PackMarketplacelog() {
    }

    public PackMarketplacelog(Long id, String action, String previousstatus, String newstatus, Long uid, Long packid, Instant createdDate) {
        this.id = id;
        this.action = action;
        this.previousstatus = previousstatus;
        this.newstatus = newstatus;
        this.uid = uid;
        this.packid = packid;
        this.createdDate = createdDate;
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

    public Long getPackid() {
        return packid;
    }

    public void setPackid(Long packid) {
        this.packid = packid;
    }
}
