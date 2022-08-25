package com.cardgame.Entity;

import javax.persistence.*;

@Entity
@Table(name = "game_room_table")
public class GameRoomTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Gameroommaster gameroommaster;

    @Column(name = "uid_one")
    private Long uidone;


    @Column(name = "uid_two")
    private Long uidtwo;

    @Column(name = "uid_three")
    private Long uidthree;

    @Column(name = "uid_four")
    private Long uidfour;

    @Column(name = "uid_five")
    private Long uidfive;

    @Column(name = "number_of_users")
    private int numberofusers;


    public GameRoomTable() {
    }

    public GameRoomTable(long id, Gameroommaster gameroommaster, Long uidone, Long uidtwo, Long uidthree, Long uidfour, Long uidfive, int numberofusers) {
        this.id = id;
        this.gameroommaster = gameroommaster;
        this.uidone = uidone;
        this.uidtwo = uidtwo;
        this.uidthree = uidthree;
        this.uidfour = uidfour;
        this.uidfive = uidfive;
        this.numberofusers = numberofusers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gameroommaster getGameroommaster() {
        return gameroommaster;
    }

    public void setGameroommaster(Gameroommaster gameroommaster) {
        this.gameroommaster = gameroommaster;
    }

    public Long getUidone() {
        return uidone;
    }

    public void setUidone(Long uidone) {
        this.uidone = uidone;
    }

    public Long getUidtwo() {
        return uidtwo;
    }

    public void setUidtwo(Long uidtwo) {
        this.uidtwo = uidtwo;
    }

    public Long getUidthree() {
        return uidthree;
    }

    public void setUidthree(Long uidthree) {
        this.uidthree = uidthree;
    }

    public Long getUidfour() {
        return uidfour;
    }

    public void setUidfour(Long uidfour) {
        this.uidfour = uidfour;
    }

    public Long getUidfive() {
        return uidfive;
    }

    public void setUidfive(Long uidfive) {
        this.uidfive = uidfive;
    }

    public int getNumberofusers() {
        return numberofusers;
    }

    public void setNumberofusers(int numberofusers) {
        this.numberofusers = numberofusers;
    }
}
