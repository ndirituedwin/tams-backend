package com.cardgame.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String wallet_could_not_be_found) {
        super(wallet_could_not_be_found);
    }
}
