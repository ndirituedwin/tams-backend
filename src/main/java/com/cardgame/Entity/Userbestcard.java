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
}
