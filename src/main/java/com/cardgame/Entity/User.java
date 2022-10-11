package com.cardgame.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "user_table")
//@JsonIgnoreProperties
public class User implements Serializable {


//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonIgnore
    @Column(unique = true)
    private Long id;

    @Column(unique = true)
    private String username;


    @Id
    private Long UID;



    @Column(name = "mobile_number",unique = true)
//    @Size(max = 10,min = 10, message = "Invalid mobile number length")
    private String mobilenumber;

    @JsonIgnore
    private String password;




    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Userwallet userwallet;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BuyIn> buyIns=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Gameroomlog> gameroomlogs=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PackPricelisting> packPricelistings=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Unopenedpack> unopenedpacks=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Userbestcard> userbestcards=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserCard> userCards=new ArrayList<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Usercardfee> usercardfees=new ArrayList<>(0);

    public User(long id, String username, Long UID) {
        this.id = id;
        this.username = username;
        this.UID = UID;
    }

    public User( String username, Long UID, String mobilenumber) {
        this.username = username;
        this.UID = UID;
        this.mobilenumber = mobilenumber;
    }

    public User(Long id, String username, Long UID, String mobilenumber, String password) {
        this.id = id;
        this.username = username;
        this.UID = UID;
        this.mobilenumber = mobilenumber;
        this.password = password;
    }

    public User(Userwallet userwallet, List<BuyIn> buyIns, List<Gameroomlog> gameroomlogs, List<PackPricelisting> packPricelistings, List<Unopenedpack> unopenedpacks, List<Userbestcard> userbestcards, List<UserCard> userCards, List<Usercardfee> usercardfees) {
        this.userwallet = userwallet;
        this.buyIns = buyIns;
        this.gameroomlogs = gameroomlogs;
        this.packPricelistings = packPricelistings;
        this.unopenedpacks = unopenedpacks;
        this.userbestcards = userbestcards;
        this.userCards = userCards;
        this.usercardfees = usercardfees;
    }

    public Userwallet getUserwallet() {
        return userwallet;
    }

    public void setUserwallet(Userwallet userwallet) {
        this.userwallet = userwallet;
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

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
