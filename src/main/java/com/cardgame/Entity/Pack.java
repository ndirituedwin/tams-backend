package com.cardgame.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pack_table")

public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pack_name")
    private String packname;

    @Column(name = "pack_type")
    private String packtype;
    @Column(name = "number_of_cards")
    private long numberofcards;

    public Pack() {
    }

    public Pack(long id, String packname, String packtype, long numberofcards) {
        this.id = id;
        this.packname = packname;
        this.packtype = packtype;
        this.numberofcards = numberofcards;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getPacktype() {
        return packtype;
    }

    public void setPacktype(String packtype) {
        this.packtype = packtype;
    }

    public long getNumberofcards() {
        return numberofcards;
    }

    public void setNumberofcards(long numberofcards) {
        this.numberofcards = numberofcards;
    }
}
