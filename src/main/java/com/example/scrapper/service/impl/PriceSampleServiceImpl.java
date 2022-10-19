package com.example.scrapper.service.impl;

import com.example.scrapper.component.PriceMapper;
import com.example.scrapper.component.UralsOilScrapper;
import com.example.scrapper.exception.throwables.PriceSampleServiceException;
import com.example.scrapper.model.dto.DateRangeDto;
import com.example.scrapper.model.dto.ExternalPriceSampleDto;
import com.example.scrapper.model.dto.MaxAndMinPriceDto;
import com.example.scrapper.model.dto.PriceSampleDto;
import com.example.scrapper.model.entity.PriceSampleEntity;
import com.example.scrapper.repository.PriceSampleRepository;
import com.example.scrapper.service.PriceSampleService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class PriceSampleServiceImpl extends ModelMapper implements PriceSampleService {

    @Autowired
    private PriceMapper priceMapper;
    @Autowired
    private UralsOilScrapper uralsOilScrapper;
    @Autowired
    private PriceSampleRepository priceSampleRepository;

    private static final String PRICE_SAMPLE_NOT_FOUND = "Price sample not found";
    private static final String PRICE_SAMPLES_NOT_FOUND = "Price samples not found";
    private static final String DATE_RANGE_IS_INVALID = "Specified date range is invalid";


    @Override
    @Transactional
    public void updateSamples() {
        List<ExternalPriceSampleDto> allExternalSamples = uralsOilScrapper.getAllSamples();
        List<PriceSampleDto> allSamples = mapExternalSamples(allExternalSamples);
        List<PriceSampleEntity> sampleEntities = mapSamplesToEntities(allSamples);
        priceSampleRepository.deleteAll();
        priceSampleRepository.saveAll(sampleEntities);
    }

    private List<PriceSampleEntity> mapSamplesToEntities(List<PriceSampleDto> allSamples) {
        return allSamples.stream()
                .map(sample -> this.map(sample, PriceSampleEntity.class))
                .collect(Collectors.toList());
    }

    private List<PriceSampleDto> mapExternalSamples(List<ExternalPriceSampleDto> allExternalSamples) {
        return allExternalSamples.stream()
                .map(priceMapper::mapExternalPriceSampleDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PriceSampleDto> getAllSamples() {
        return priceSampleRepository.findAll().stream()
                .map(sampleEntity -> this.map(sampleEntity, PriceSampleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public Double getAveragePriceOfSpecificSampleByDate(LocalDate localDate) {
        Optional<PriceSampleEntity> priceSample = priceSampleRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(localDate);
        if (priceSample.isEmpty()) {
            throw new PriceSampleServiceException(PRICE_SAMPLE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return priceSample.get().getAveragePrice();
    }

    @Override
    @SneakyThrows
    public Double getAveragePriceOfSpecificSamplesInRange(DateRangeDto dateRangeDto) {
        if (dateRangeIsInvalid(dateRangeDto)) {
            throw new PriceSampleServiceException(DATE_RANGE_IS_INVALID, HttpStatus.BAD_REQUEST);
        }
        List<PriceSampleEntity> priceSamples = priceSampleRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(dateRangeDto.getStartDate(), dateRangeDto.getEndDate());
        OptionalDouble average = priceSamples.stream().mapToDouble(PriceSampleEntity::getAveragePrice).average();
        if (average.isEmpty()) {
            throw new PriceSampleServiceException(PRICE_SAMPLES_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return average.getAsDouble();
    }


    @Override
    @SneakyThrows
    public MaxAndMinPriceDto getMaxAndMinPriceOfSpecificSamplesInRange(DateRangeDto dateRangeDto) {
        if (dateRangeIsInvalid(dateRangeDto)) {
            throw new PriceSampleServiceException(DATE_RANGE_IS_INVALID, HttpStatus.BAD_REQUEST);
        }
        List<PriceSampleEntity> priceSamples = priceSampleRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(dateRangeDto.getStartDate(), dateRangeDto.getEndDate());
        if (priceSamples.isEmpty()) {
            throw new PriceSampleServiceException(PRICE_SAMPLES_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Double min = priceSamples.stream().mapToDouble(PriceSampleEntity::getAveragePrice).min().getAsDouble();
        Double max = priceSamples.stream().mapToDouble(PriceSampleEntity::getAveragePrice).max().getAsDouble();
        return new MaxAndMinPriceDto(min, max);
    }

    private boolean dateRangeIsInvalid(DateRangeDto dateRangeDto) {
        return dateRangeDto.getEndDate().isBefore(dateRangeDto.getStartDate());
    }
}
