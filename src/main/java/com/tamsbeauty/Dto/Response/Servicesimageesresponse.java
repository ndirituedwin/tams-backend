package com.tamsbeauty.Dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamsbeauty.Entity.ServiceImage;
import com.tamsbeauty.Entity.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servicesimageesresponse {

    @JsonProperty
    private Services services;

    private List<ServiceImage> serviceImageList;

}
