//package com.cardgame.Entity;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.time.Instant;
//
//@Table(name ="game_rooms_table")
//@Entity
//public class GameRoom {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "created_at")
//    private Instant createddate;
//    @Column(name = "is_active")
//    private boolean status=false;
//    @Column(name = "minimum_amount")
//    private BigDecimal minimumamount;
//    @Column(name ="number_of_users")
//    private int numberofusers;
//
//    public GameRoom() {
//    }
//
//    public GameRoom(Long id, Instant createddate, boolean status, BigDecimal minimumamount, int numberofusers) {
//        this.id = id;
//        this.createddate = createddate;
//        this.status = status;
//        this.minimumamount = minimumamount;
//        this.numberofusers = numberofusers;
//
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Instant getCreateddate() {
//        return createddate;
//    }
//
//    public void setCreateddate(Instant createddate) {
//        this.createddate = createddate;
//    }
//
//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }
//
//    public BigDecimal getMinimumamount() {
//        return minimumamount;
//    }
//
//    public void setMinimumamount(BigDecimal minimumamount) {
//        this.minimumamount = minimumamount;
//    }
//
//    public int getNumberofusers() {
//        return numberofusers;
//    }
//
//    public void setNumberofusers(int numberofusers) {
//        this.numberofusers = numberofusers;
//    }
//}
