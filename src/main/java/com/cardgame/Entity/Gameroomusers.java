//package com.cardgame.Entity;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "game_room_users")
//public class Gameroomusers {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private GameRoom gameRoom;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    private Userwallet userwallet;
//
//    public Gameroomusers() {
//    }
//
//
//    public Gameroomusers(long id, GameRoom gameRoom, User user, Userwallet userwallet) {
//        this.id = id;
//        this.gameRoom = gameRoom;
//        this.user = user;
//        this.userwallet = userwallet;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public Userwallet getUserwallet() {
//        return userwallet;
//    }
//
//    public void setUserwallet(Userwallet userwallet) {
//        this.userwallet = userwallet;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public GameRoom getGameRoom() {
//        return gameRoom;
//    }
//
//    public void setGameRoom(GameRoom gameRoom) {
//        this.gameRoom = gameRoom;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//}
