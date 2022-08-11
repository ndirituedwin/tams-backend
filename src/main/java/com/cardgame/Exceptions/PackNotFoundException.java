package com.cardgame.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotBlank;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PackNotFoundException extends RuntimeException {
    public PackNotFoundException(String s) {
        super(s);
    }
}
