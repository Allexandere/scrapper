package com.example.scrapper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceSampleDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Double averagePrice;
}
