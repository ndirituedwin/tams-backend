package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Cardduplicaterequest {

    @NotBlank(message = "card id may not be blank")
    private String cardid;
    @NotBlank(message = "number of replications may not be blank")
    private String numberofreplications;


}
