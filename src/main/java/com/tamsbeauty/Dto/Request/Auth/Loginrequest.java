package com.tamsbeauty.Dto.Request.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Loginrequest {


    @NotBlank(message = "mobile may not be blank")
    private String mobile;

    @NotBlank(message = "password may not be blank")
    private String password;


}
