package com.cardgame.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "player_best_card_logs")
public class Playerbestcardlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "best_card_id")
    private Long bestcardid;

    @Column(name = "user_id")
    private Long userid;
    @Column(name = "user_card_id")
    private Long usercardid;

    private String action;
    @Column(name = "created_date")
    private Instant createdat;

    public Playerbestcardlog() {
    }

    public Playerbestcardlog(Long id, Long bestcardid, Long userid, Long usercardid, String action, Instant createdat) {
        this.id = id;
        this.bestcardid = bestcardid;
        this.userid = userid;
        this.usercardid = usercardid;
        this.action = action;
        this.createdat = createdat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBestcardid() {
        return bestcardid;
    }

    public void setBestcardid(Long bestcardid) {
        this.bestcardid = bestcardid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Long getUsercardid() {
        return usercardid;
    }

    public void setUsercardid(Long usercardid) {
        this.usercardid = usercardid;
    }
}
