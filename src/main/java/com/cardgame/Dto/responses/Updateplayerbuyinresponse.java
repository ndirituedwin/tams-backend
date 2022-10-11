package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class Updateplayerbuyinresponse {


    private String message;
    private Long uid;
    private BigDecimal buyin;
    private Long buyInId;
    private String status;
    private Integer numberofplayers;



    public Updateplayerbuyinresponse() {
    }

    public Updateplayerbuyinresponse(String message, Long uid, BigDecimal buyin, Long buyInId, String status, Integer numberofplayers) {
        this.message = message;
        this.uid = uid;
        this.buyin = buyin;
        this.buyInId = buyInId;
        this.status = status;
        this.numberofplayers = numberofplayers;
    }

    public Updateplayerbuyinresponse(String message, Long uid, BigDecimal buyin, Long buyInId, String status) {
        this.message = message;
        this.uid = uid;
        this.buyin = buyin;
        this.buyInId = buyInId;
        this.status = status;
    }

    public Updateplayerbuyinresponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public BigDecimal getBuyin() {
        return buyin;
    }

    public void setBuyin(BigDecimal buyin) {
        this.buyin = buyin;
    }

    public Long getBuyInId() {
        return buyInId;
    }

    public void setBuyInId(Long buyInId) {
        this.buyInId = buyInId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNumberofplayers() {
        return numberofplayers;
    }

    public void setNumberofplayers(Integer numberofplayers) {
        this.numberofplayers = numberofplayers;
    }
}
