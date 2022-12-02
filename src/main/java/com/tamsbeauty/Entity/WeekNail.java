package com.tamsbeauty.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nails_of_the_week")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekNail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String description;
private BigDecimal price;
private String image;
}
