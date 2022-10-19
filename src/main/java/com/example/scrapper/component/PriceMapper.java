package com.example.scrapper.component;

import com.example.scrapper.model.dto.ExternalPriceSampleDto;
import com.example.scrapper.model.dto.PriceSampleDto;

public interface PriceMapper {
    PriceSampleDto mapExternalPriceSampleDto(ExternalPriceSampleDto externalPriceSampleDto);
}
