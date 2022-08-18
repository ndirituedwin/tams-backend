package com.cardgame.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_cards")
@Data
//@AllArgsConstructor
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    private Cardduplicate cardduplicate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pack pack;

    @Column(name = "opened_card")
    private boolean openedcard=false;


    public UserCard(User user, Cardduplicate cardduplicate, Pack pack, boolean openedcard) {
        this.user = user;
        this.cardduplicate = cardduplicate;
        this.pack = pack;
        this.openedcard = openedcard;
    }

    public UserCard() {
    }

    public UserCard(Long id, User user, Cardduplicate cardduplicate) {
        this.id = id;
        this.user = user;
        this.cardduplicate = cardduplicate;
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

    public Cardduplicate getCardduplicate() {
        return cardduplicate;
    }


    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public boolean isOpenedcard() {
        return openedcard;
    }

    public void setOpenedcard(boolean openedcard) {
        this.openedcard = openedcard;
    }


    public void setCardduplicate(Cardduplicate cardduplicate) {
        this.cardduplicate = cardduplicate;
    }
}
