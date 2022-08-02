package com.cardgame.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    private Cardduplicate cardduplicate;

    public UserCard(User user, Cardduplicate cardduplicate) {
        this.user=user;
        this.cardduplicate=cardduplicate;
    }
}
