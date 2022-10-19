package com.example.scrapper.service;

import com.example.scrapper.model.dto.DateRangeDto;
import com.example.scrapper.model.dto.MaxAndMinPriceDto;
import com.example.scrapper.model.dto.PriceSampleDto;

import java.time.LocalDate;
import java.util.List;

public interface PriceSampleService {
    void updateSamples();

    List<PriceSampleDto> getAllSamples();

    Double getAveragePriceOfSpecificSampleByDate(LocalDate localDate);

    Double getAveragePriceOfSpecificSamplesInRange(DateRangeDto dateRangeDto);

    MaxAndMinPriceDto getMaxAndMinPriceOfSpecificSamplesInRange(DateRangeDto dateRangeDto);

}
