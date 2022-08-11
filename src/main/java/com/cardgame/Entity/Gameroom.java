//package com.cardgame.Entity;
//
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//import javax.validation.constraints.Null;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.Date;
//
//@Entity
//@Table(name = "game_rooms")
//public class Gameroom {
//
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
//    @Column(name = "user_one")
//    @Nullable
//    private Long userone;
//    @Column(name = "user_two")
//    @Nullable
//    private Long usertwo;
//    @Column(name = "user_three")
//    @Nullable
//    private Long userthree;
//    @Column(name = "user_four")
//    @Nullable
//    private Long userfour;
//    @Column(name = "user_five")
//    @Nullable
//    private Long userfive;
//
//    @Column(name = "number_of_users")
//    private Long numberofusers;
//
//    public Gameroom() {
//    }
//
//    public Gameroom(Long id, Instant createddate, boolean status, BigDecimal minimumamount, @Nullable Long userone, @Nullable Long usertwo, @Nullable Long userthree, @Nullable Long userfour, @Nullable Long userfive, Long numberofusers) {
//        this.id = id;
//        this.createddate = createddate;
//        this.status = status;
//        this.minimumamount = minimumamount;
//        this.userone = userone;
//        this.usertwo = usertwo;
//        this.userthree = userthree;
//        this.userfour = userfour;
//        this.userfive = userfive;
//        this.numberofusers = numberofusers;
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
//    public Long getUserone() {
//        return userone;
//    }
//
//    public void setUserone(Long userone) {
//        this.userone = userone;
//    }
//
//    public Long getUsertwo() {
//        return usertwo;
//    }
//
//    public void setUsertwo(Long usertwo) {
//        this.usertwo = usertwo;
//    }
//
//    public Long getUserthree() {
//        return userthree;
//    }
//
//    public void setUserthree(Long userthree) {
//        this.userthree = userthree;
//    }
//
//    public Long getUserfour() {
//        return userfour;
//    }
//
//    public void setUserfour(Long userfour) {
//        this.userfour = userfour;
//    }
//
//    public Long getUserfive() {
//        return userfive;
//    }
//
//    public void setUserfive(Long userfive) {
//        this.userfive = userfive;
//    }
//
//    public Long getNumberofusers() {
//        return numberofusers;
//    }
//
//    public void setNumberofusers(Long numberofusers) {
//        this.numberofusers = numberofusers;
//    }
//}
