package com.tamsbeauty.Dto.Request.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Signuprequest {

    @NotBlank(message = " name may not be blank")
    private String name;
    @NotBlank(message = "user name may not be blank")
    private String username;
    @NotBlank(message = "password may not be blank")
    private String password;
    @NotBlank(message = "mobile may not be blank")
    @Size(max = 10,min = 10, message = "mobile length is invalid")
    private String  mobile;

}
