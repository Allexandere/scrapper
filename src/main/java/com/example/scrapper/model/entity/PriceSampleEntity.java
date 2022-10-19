package com.example.scrapper.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Table(name = "price_sample")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PriceSampleEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(columnDefinition = "DATE")
    private LocalDate startDate;
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;
    private Double averagePrice;

    public PriceSampleEntity(LocalDate startDate, LocalDate endDate, Double averagePrice) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.averagePrice = averagePrice;
    }
}


