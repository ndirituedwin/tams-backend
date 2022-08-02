package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Cardbyratioduplicaterequest {

    @NotBlank(message = "card id may not be blank")
    private String cardid;
    @NotBlank(message = "ratio")
    private String ratio;


}
