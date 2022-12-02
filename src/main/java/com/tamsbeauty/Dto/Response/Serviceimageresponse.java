package com.tamsbeauty.Dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamsbeauty.Entity.ServiceImage;
import com.tamsbeauty.Entity.Services;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Serviceimageresponse {


    private ServiceImage serviceImage;
    private String message;
    private boolean status;

}
