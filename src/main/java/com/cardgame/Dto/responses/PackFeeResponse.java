package com.cardgame.Dto.responses;

import java.math.BigDecimal;

public class PackFeeResponse {

    private String packname;

    private String packtype;
    private BigDecimal amount;
    private Long user;

    public PackFeeResponse() {
    }

    public PackFeeResponse(String packname, String packtype, BigDecimal amount, Long user) {
        this.packname = packname;
        this.packtype = packtype;
        this.amount = amount;
        this.user = user;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
