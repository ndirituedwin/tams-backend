package com.cardgame.Dto.requests.Auth;


import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class Loginrequest {


    @NotBlank(message = "mobile number may not be blank")
    private String mobilenumber;

    private String password;

    public Loginrequest() {
    }

    public Loginrequest(String mobilenumber, String password) {
        this.mobilenumber = mobilenumber;
        this.password = password;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
