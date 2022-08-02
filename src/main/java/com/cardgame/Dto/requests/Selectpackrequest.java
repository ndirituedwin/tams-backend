package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Selectpackrequest {


    @NotBlank(message = "select pack")
    private String pack;

}
