package com.cardgame.Entity;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game_room_logs")
public class Gameroomlog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Instant createddate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gameroommaster gameroommaster;
    @ManyToOne(fetch = FetchType.LAZY)
    private GameRoomTable gameRoomTable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
//            name = "UID",
            referencedColumnName = "UID"
    )
    private User user;


    @Column(name = "number_of_participants")
    private Integer numberofparticipants;

    @Column(name = "action")
    private String action;

    public Gameroomlog() {
    }

    public Gameroomlog(Long id, Instant createddate, Gameroommaster gameroommaster, GameRoomTable gameRoomTable, User user, Integer numberofparticipants, String action) {
        this.id = id;
        this.createddate = createddate;
        this.gameroommaster = gameroommaster;
        this.gameRoomTable = gameRoomTable;
        this.user = user;
        this.numberofparticipants = numberofparticipants;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
    }

    public Gameroommaster getGameroommaster() {
        return gameroommaster;
    }

    public void setGameroommaster(Gameroommaster gameroommaster) {
        this.gameroommaster = gameroommaster;
    }

    public GameRoomTable getGameRoomTable() {
        return gameRoomTable;
    }

    public void setGameRoomTable(GameRoomTable gameRoomTable) {
        this.gameRoomTable = gameRoomTable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumberofparticipants() {
        return numberofparticipants;
    }

    public void setNumberofparticipants(Integer numberofparticipants) {
        this.numberofparticipants = numberofparticipants;
    }
}
