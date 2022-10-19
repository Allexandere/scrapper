package com.example.scrapper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExternalPriceSampleDto {
    private String startDate;
    private String endDate;
    private String averagePrice;
}
