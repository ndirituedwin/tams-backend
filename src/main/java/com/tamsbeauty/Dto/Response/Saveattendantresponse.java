package com.tamsbeauty.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Saveattendantresponse {

    private boolean status;
    private String message;
    private String attendant;
}
