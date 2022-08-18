package com.cardgame.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectPackRequestResponse {

private String message;

    public SelectPackRequestResponse(String message) {
        this.message = message;
    }

    public SelectPackRequestResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
