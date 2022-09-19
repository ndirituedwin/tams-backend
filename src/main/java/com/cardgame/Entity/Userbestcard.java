package com.cardgame.Entity;

import com.cardgame.Exceptions.UserCardNotFoundException;
import com.cardgame.Repo.UserCardRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "userbest_cards")
//@JsonIgnoreProperties(value = {“hibernateLazyInitializer”,“handler”})
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Userbestcard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    @Column(name = "userbestcard_id")
    @Column(name = "user_card_id")
    private Long userbestcard;

    //    @JsonIgnore
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="usercard_id")
    @OneToOne(fetch = FetchType.LAZY)
    private UserCard  userCard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "UID")
    private User user;

    public Userbestcard() {
    }

//    public Userbestcard(long id, Long userbestcard, User user) {
//        this.id = id;
//        this.userbestcard = userbestcard;
//        this.user = user;
//    }


    // public Userbestcard(long id, Long userbestcard, User user) {
    //     this.id = id;
    //     this.userbestcard = userbestcard;
    //     this.user = user;
    // }





    public Userbestcard(long id, Long userbestcard, UserCard userCard, User user) {
        this.id = id;
        this.userbestcard = userbestcard;
        this.userCard = userCard;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserbestcard() {

        return userbestcard;
    }

    public void setUserbestcard(Long userbestcard) {
        this.userbestcard = userbestcard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCard getUserCard() {
        return userCard;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }
}

