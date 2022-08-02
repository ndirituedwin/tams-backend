package com.cardgame.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "card_duplicates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cardduplicate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    private boolean isTaken=false;

    public Cardduplicate(Card card) {
        this.card = card;
    }
}
