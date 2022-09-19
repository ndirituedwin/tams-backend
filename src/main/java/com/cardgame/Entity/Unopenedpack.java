package com.cardgame.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.springframework.core.serializer.Serializer;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer"})

@Entity
@Table(name = "purchased_packs")
public class Unopenedpack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pack pack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "UID")
    private User user;

    @Column(name = "is_open")
    private boolean isopen=false;

    public Unopenedpack() {
    }

    public Unopenedpack(Pack pack, User user, boolean isopen) {
        this.pack = pack;
        this.user = user;
        this.isopen = isopen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isIsopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }
}
