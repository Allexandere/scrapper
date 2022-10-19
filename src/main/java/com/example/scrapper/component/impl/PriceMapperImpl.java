package com.example.scrapper.component.impl;

import com.example.scrapper.component.PriceMapper;
import com.example.scrapper.component.RuMonths;
import com.example.scrapper.model.dto.ExternalPriceSampleDto;
import com.example.scrapper.model.dto.PriceSampleDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PriceMapperImpl implements PriceMapper {

    private static final Integer BASE_YEAR = 2000;

    @Override
    public PriceSampleDto mapExternalPriceSampleDto(ExternalPriceSampleDto externalPriceSampleDto) {
        return PriceSampleDto.builder()
                .averagePrice(Double.parseDouble(externalPriceSampleDto.getAveragePrice().replace(',', '.')))
                .startDate(parseExternalDate(externalPriceSampleDto.getStartDate()))
                .endDate(parseExternalDate(externalPriceSampleDto.getEndDate()))
                .build();
    }

    private LocalDate parseExternalDate(String startDate) {
        String[] date = startDate.split("\\.");
        int day = Integer.parseInt(date[0]);
        Integer month = RuMonths.getMonthNumberByTitle(date[1]);
        int year = BASE_YEAR + Integer.parseInt(date[2]);
        return LocalDate.of(year, month, day);
    }
}
