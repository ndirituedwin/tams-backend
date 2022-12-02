package com.tamsbeauty.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Saveattendantrequest {


    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 10,min = 10)
    private String mobile;

}
