package com.cardgame.Dto.responses.Auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Signuprequest {

    @NotBlank(message = "user name may not be blank")
    private String username;
    @NotBlank(message = "user name may not be blank")
    private String password;
    @NotBlank(message = "user name may not be blank")
    @Size(max = 10,min = 10, message = "mobile length is invalid")
    private String  mobile;

    public Signuprequest(String username, String password, String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
