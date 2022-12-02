package com.tamsbeauty.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Servicesresponse {

    private String  message;
    private Long id;
    private boolean status;
}
