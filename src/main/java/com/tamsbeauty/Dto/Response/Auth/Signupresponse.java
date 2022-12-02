package com.tamsbeauty.Dto.Response.Auth;


public class Signupresponse {

    private String message;
    private String username;
    private String  mobile;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Signupresponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public Signupresponse(String message, String username, String mobile, boolean status) {
        this.message = message;
        this.username = username;
        this.mobile = mobile;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
