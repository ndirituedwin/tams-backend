package com.tamsbeauty.Dto.Request.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenRequest {


    @NotBlank
    private String refreshToken;


}
