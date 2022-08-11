package com.cardgame.Dto.responses;


import java.math.BigDecimal;

public class Userwalletbalance {


    private BigDecimal totalwalletbalance;
    private String message;

    public Userwalletbalance(BigDecimal totalwalletbalance) {
        this.totalwalletbalance = totalwalletbalance;
    }

    public Userwalletbalance(String message) {
        this.message = message;
    }

    public BigDecimal getTotalwalletbalance() {
        return totalwalletbalance;
    }

    public void setTotalwalletbalance(BigDecimal totalwalletbalance) {
        this.totalwalletbalance = totalwalletbalance;
    }
}
