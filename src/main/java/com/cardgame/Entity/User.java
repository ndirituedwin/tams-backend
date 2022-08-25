package com.cardgame.Entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {


//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(unique = true)
    private Long id;

    private String username;


    @Id
    private Long UID;

    @OneToMany(mappedBy = "id")
    private List<BuyIn> buyIns=new ArrayList<>(0);

    @OneToMany(mappedBy = "id")
    private List<Gameroomlog> gameroomlogs=new ArrayList<>(0);

    @OneToMany(mappedBy = "id")
    private List<PackPricelisting> packPricelistings=new ArrayList<>(0);
    @OneToMany(mappedBy = "id")
    private List<Unopenedpack> unopenedpacks=new ArrayList<>(0);
    @OneToMany(mappedBy = "id")
    private List<Userbestcard> userbestcards=new ArrayList<>(0);
    @OneToMany(mappedBy = "id")
    private List<UserCard> userCards=new ArrayList<>(0);
    @OneToMany(mappedBy = "id")
    private List<Usercardfee> usercardfees=new ArrayList<>(0);

    public User(long id, String username, Long UID) {
        this.id = id;
        this.username = username;
        this.UID = UID;
    }

    public User(List<BuyIn> buyIns, List<Gameroomlog> gameroomlogs, List<PackPricelisting> packPricelistings, List<Unopenedpack> unopenedpacks, List<Userbestcard> userbestcards, List<UserCard> userCards, List<Usercardfee> usercardfees) {
        this.buyIns = buyIns;
        this.gameroomlogs = gameroomlogs;
        this.packPricelistings = packPricelistings;
        this.unopenedpacks = unopenedpacks;
        this.userbestcards = userbestcards;
        this.userCards = userCards;
        this.usercardfees = usercardfees;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUID() {
        return UID;
    }

    public void setUID(long UID) {
        this.UID = UID;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUID(Long UID) {
        this.UID = UID;
    }

    public List<BuyIn> getBuyIns() {
        return buyIns;
    }

    public void setBuyIns(List<BuyIn> buyIns) {
        this.buyIns = buyIns;
    }

    public List<Gameroomlog> getGameroomlogs() {
        return gameroomlogs;
    }

    public void setGameroomlogs(List<Gameroomlog> gameroomlogs) {
        this.gameroomlogs = gameroomlogs;
    }

    public List<PackPricelisting> getPackPricelistings() {
        return packPricelistings;
    }

    public void setPackPricelistings(List<PackPricelisting> packPricelistings) {
        this.packPricelistings = packPricelistings;
    }

    public List<Unopenedpack> getUnopenedpacks() {
        return unopenedpacks;
    }

    public void setUnopenedpacks(List<Unopenedpack> unopenedpacks) {
        this.unopenedpacks = unopenedpacks;
    }

    public List<Userbestcard> getUserbestcards() {
        return userbestcards;
    }

    public void setUserbestcards(List<Userbestcard> userbestcards) {
        this.userbestcards = userbestcards;
    }

    public List<UserCard> getUserCards() {
        return userCards;
    }

    public void setUserCards(List<UserCard> userCards) {
        this.userCards = userCards;
    }

    public List<Usercardfee> getUsercardfees() {
        return usercardfees;
    }

    public void setUsercardfees(List<Usercardfee> usercardfees) {
        this.usercardfees = usercardfees;
    }
}
