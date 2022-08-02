package com.cardgame.Dto.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cardduplicateresponse {

private String message;
private long cardid;
}
