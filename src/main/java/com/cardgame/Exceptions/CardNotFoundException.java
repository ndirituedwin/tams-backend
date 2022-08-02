package com.cardgame.Exceptions;


public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String s) {
        super(s);
    }
}
