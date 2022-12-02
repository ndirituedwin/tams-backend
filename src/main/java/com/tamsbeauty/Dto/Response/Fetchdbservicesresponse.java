package com.tamsbeauty.Dto.Response;

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
public class Fetchdbservicesresponse {

    private List<?> servicesList;
    private boolean status;
    private String message;

}
