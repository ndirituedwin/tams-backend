package com.cardgame.Entity;

import javax.persistence.*;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;


    private long UID;

}
