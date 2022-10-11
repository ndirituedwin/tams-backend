package com.cardgame.Entity;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "booster_pack_status")
//@Table(name = "booster_pack_status",uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "isActive" }) })
public class Boosterpackstatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User   user;

    private boolean pack1=false;
    private boolean pack2=false;
    private boolean pack3=false;
    private Instant createdAt=Instant.now();

    public Boosterpackstatus() {
    }

    public Boosterpackstatus(Long id, User user, boolean pack1, boolean pack2, boolean pack3, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.pack1 = pack1;
        this.pack2 = pack2;
        this.pack3 = pack3;
        this.createdAt = createdAt;
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

    public boolean isPack1() {
        return pack1;
    }

    public void setPack1(boolean pack1) {
        this.pack1 = pack1;
    }

    public boolean isPack2() {
        return pack2;
    }

    public void setPack2(boolean pack2) {
        this.pack2 = pack2;
    }

    public boolean isPack3() {
        return pack3;
    }

    public void setPack3(boolean pack3) {
        this.pack3 = pack3;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
