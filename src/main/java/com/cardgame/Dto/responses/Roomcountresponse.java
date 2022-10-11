package com.cardgame.Dto.responses;

public class Roomcountresponse {


    private int numberofplayers;
    private String message;
    

    
    public Roomcountresponse(String message) {
        this.message = message;
    }

    public Roomcountresponse(int numberofplayers) {
        this.numberofplayers = numberofplayers;
    }

    public int getNumberofplayers() {
        return numberofplayers;
    }

    public void setNumberofplayers(int numberofplayers) {
        this.numberofplayers = numberofplayers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

    
  

    

}
