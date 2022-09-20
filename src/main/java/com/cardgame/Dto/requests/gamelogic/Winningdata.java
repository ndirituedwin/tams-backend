package com.cardgame.Dto.requests.gamelogic;

import java.util.ArrayList;

public class Winningdata {


    private Gamewinnerrequest gamewinnerrequest;
    private ArrayList<ArrayList<ArrayList<Winninghandrequest>>> winninghandrequest;

    public Winningdata() {
    }

    public Winningdata(Gamewinnerrequest gamewinnerrequest, ArrayList<ArrayList<ArrayList<Winninghandrequest>>> winninghandrequest) {
        this.gamewinnerrequest = gamewinnerrequest;
        this.winninghandrequest = winninghandrequest;
    }

    public Gamewinnerrequest getGamewinnerrequest() {
        return gamewinnerrequest;
    }

    public void setGamewinnerrequest(Gamewinnerrequest gamewinnerrequest) {
        this.gamewinnerrequest = gamewinnerrequest;
    }

    public ArrayList<ArrayList<ArrayList<Winninghandrequest>>> getWinninghandrequest() {
        return winninghandrequest;
    }

    public void setWinninghandrequest(ArrayList<ArrayList<ArrayList<Winninghandrequest>>> winninghandrequest) {
        this.winninghandrequest = winninghandrequest;
    }
}
