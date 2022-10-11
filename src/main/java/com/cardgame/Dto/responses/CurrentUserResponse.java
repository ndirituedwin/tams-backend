package com.cardgame.Dto.responses;

public class CurrentUserResponse {

    private Long id;
    private Long uid;
    private String  username;
    private String mobilenumber;

    private String  message;

    public CurrentUserResponse(String message) {
        this.message = message;
    }

    public CurrentUserResponse(Long id, Long uid, String username, String mobilenumber) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.mobilenumber = mobilenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
