package com.cardgame.Dto.requests.gamelogic;

public class CardPairingtest {


    private String uid140cards;
    private String uid199cards;


    public CardPairingtest(String uid140cards, String uid199cards) {
        this.uid140cards = uid140cards;
        this.uid199cards = uid199cards;
    }

    public String getUid140cards() {
        return uid140cards;
    }

    public void setUid140cards(String uid140cards) {
        this.uid140cards = uid140cards;
    }

    public String getUid199cards() {
        return uid199cards;
    }

    public void setUid199cards(String uid199cards) {
        this.uid199cards = uid199cards;
    }
}
