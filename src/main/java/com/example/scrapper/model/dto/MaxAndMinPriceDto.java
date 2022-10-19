package com.example.scrapper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MaxAndMinPriceDto {
    private Double min;
    private Double max;
}
