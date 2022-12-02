package com.tamsbeauty.Dto.Response;

import com.tamsbeauty.Entity.WeekNail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nailofweekresponse {


    private boolean status;
    private String message;
    private String description;
    private List<?> weekNailList;
}
