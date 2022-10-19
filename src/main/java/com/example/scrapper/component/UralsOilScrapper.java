package com.example.scrapper.component;

import com.example.scrapper.model.dto.ExternalPriceSampleDto;

import java.util.List;

public interface UralsOilScrapper {
    List<ExternalPriceSampleDto> getAllSamples();
}
