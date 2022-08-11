package com.cardgame.Dto.responses;

public class UserCardResponse {


    private Long id;
    private Long user;
    private Long cardduplicate;
    private Long pack;

    public UserCardResponse() {
    }

    public UserCardResponse(Long id, Long user, Long cardduplicate, Long pack) {
        this.id = id;
        this.user = user;
        this.cardduplicate = cardduplicate;
        this.pack = pack;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getCardduplicate() {
        return cardduplicate;
    }

    public void setCardduplicate(Long cardduplicate) {
        this.cardduplicate = cardduplicate;
    }

    public Long getPack() {
        return pack;
    }

    public void setPack(Long pack) {
        this.pack = pack;
    }
}
