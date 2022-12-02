package com.tamsbeauty.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class Bookingrequest {




    private BigDecimal amountrequired;
    private BigDecimal amount;
    private String timepaid;
    private String mobile;
    private String servicetime;
    private Long services_id;
    private Long attendant_id;



}
