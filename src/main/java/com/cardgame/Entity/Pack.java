package com.cardgame.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer"})

@Entity
@Table(name = "pack_table")

public class Pack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pack_name")
    private String packname;

    @Column(name = "pack_type")
    private String packtype;
    @Column(name = "number_of_cards")
    private long numberofcards;

    private String image;

    private BigDecimal price;
    private Long gold;
    private Long silver;
    private Long bronze;
    private Long trainer;

    public Pack() {
    }

    public Pack(String image, BigDecimal price, Long gold, Long silver, Long bronze, Long trainer) {
        this.image = image;
        this.price = price;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.trainer = trainer;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getGold() {
        return gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public Long getSilver() {
        return silver;
    }

    public void setSilver(Long silver) {
        this.silver = silver;
    }

    public Long getBronze() {
        return bronze;
    }

    public void setBronze(Long bronze) {
        this.bronze = bronze;
    }

    public Long getTrainer() {
        return trainer;
    }

    public void setTrainer(Long trainer) {
        this.trainer = trainer;
    }
}
