package com.cardgame.Dto.responses;

import com.cardgame.Entity.Pack;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;

public class Marketplacecardsresponse {



    private long id;
    private String packname;
    private String packtype;
    private long numberofcards;
    private String image;
    private BigDecimal price;
    private Long gold;
    private Long silver;
    private Long bronze;
    private Long trainer;

    public Marketplacecardsresponse() {
    }

    public Marketplacecardsresponse(long id, String packname, String packtype, long numberofcards, String image, BigDecimal price, Long gold, Long silver, Long bronze, Long trainer) {
        this.id = id;
        this.packname = packname;
        this.packtype = packtype;
        this.numberofcards = numberofcards;
        this.image = image;
        this.price = price;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.trainer = trainer;
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

    /**private Pack pack;

    public Marketplacecardsresponse() {
    }

    public Marketplacecardsresponse(Pack pack) {
        this.pack = pack;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }*/
}
