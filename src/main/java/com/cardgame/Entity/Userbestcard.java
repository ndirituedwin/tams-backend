package com.cardgame.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "userbest_cards")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userbestcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "userbestcard_id")
    private Long userbestcard;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

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
}
