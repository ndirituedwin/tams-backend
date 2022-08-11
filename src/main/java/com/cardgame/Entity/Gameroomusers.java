package com.cardgame.Entity;

import javax.persistence.*;

@Entity
@Table(name = "game_room_users")
public class Gameroomusers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameRoom gameRoom;

    @Column(name = "user_id")
    private Long userid;

    public Gameroomusers() {
    }

    public Gameroomusers(long id, GameRoom gameRoom, Long userid) {
        this.id = id;
        this.gameRoom = gameRoom;
        this.userid = userid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
