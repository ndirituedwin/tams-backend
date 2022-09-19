package com.cardgame.Dto.responses;


import java.math.BigDecimal;

public class Userwalletbalance {


    private BigDecimal total_wallet_balance;
    private Long id;
    private Long user_id;
    private String message;

    public Userwalletbalance() {
    }

    public Userwalletbalance(BigDecimal total_wallet_balance, Long id, Long user_id) {
        this.total_wallet_balance = total_wallet_balance;
        this.id = id;
        this.user_id = user_id;
    }

    public Userwalletbalance(String message) {
        this.message = message;
    }

    public BigDecimal getTotal_wallet_balance() {
        return total_wallet_balance;
    }

    public void setTotal_wallet_balance(BigDecimal total_wallet_balance) {
        this.total_wallet_balance = total_wallet_balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
