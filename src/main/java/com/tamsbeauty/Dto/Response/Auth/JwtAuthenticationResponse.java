package com.tamsbeauty.Dto.Response.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {


    private String accessToken;
    private String refreshToken;
    private String tokenType="Bearer";
    private String username;
    private Instant expirationTime;
    private String message;
    private boolean status;

}
