package com.cardgame.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer"})

@Entity
@Table(name = "user_cards")
@Data
//@AllArgsConstructor
public class UserCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "UID")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    private Cardduplicate cardduplicate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unopenedpack unopenedpack;

    @Column(name = "opened_card")
    private boolean openedcard=false;

//    @JsonIgnore
//    @OneToMany(mappedBy = "userCard")
//    private List<Userbestcard> userbestcards=new ArrayList<>(0);

    public UserCard(User user, Cardduplicate cardduplicate, Unopenedpack unopenedpack, boolean openedcard) {
        this.user = user;
        this.cardduplicate = cardduplicate;
        this.unopenedpack = unopenedpack;
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


    public Unopenedpack getUnopenedpack() {
        return unopenedpack;
    }

    public void setUnopenedpack(Unopenedpack unopenedpack) {
        this.unopenedpack = unopenedpack;
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
