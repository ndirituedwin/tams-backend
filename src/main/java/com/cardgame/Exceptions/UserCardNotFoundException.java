package com.cardgame.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserCardNotFoundException extends RuntimeException {
    public UserCardNotFoundException(String s) {
        super(s);
    }
}
