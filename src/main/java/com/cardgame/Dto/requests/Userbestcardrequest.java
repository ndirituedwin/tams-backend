package com.cardgame.Dto.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Userbestcardrequest {


@NotBlank(message = "best cards may not be blank")
private String userbestcards;

}
