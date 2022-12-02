package com.tamsbeauty.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Services services;

    @ManyToOne(fetch =FetchType.LAZY)
    private User user;
    @ManyToOne(fetch =FetchType.LAZY)
    private Attendant attendant;

    private String mobile;

    private BigDecimal amountRequired;
    private BigDecimal amount;
    private String  timepaid;

    private Instant createddate=Instant.now();

    private String  servicetime;
    
}
