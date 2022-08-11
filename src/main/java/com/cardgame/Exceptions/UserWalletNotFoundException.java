package com.cardgame.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserWalletNotFoundException extends RuntimeException {
    public UserWalletNotFoundException(String user_wallet_not_not_found) {
        super(user_wallet_not_not_found);
    }
}
