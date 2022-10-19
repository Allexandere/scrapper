package com.example.scrapper.tests;

import com.example.scrapper.BaseTest;
import com.example.scrapper.component.UralsOilScrapper;
import com.example.scrapper.exception.throwables.PriceSampleServiceException;
import com.example.scrapper.model.dto.DateRangeDto;
import com.example.scrapper.model.dto.ExternalPriceSampleDto;
import com.example.scrapper.model.dto.MaxAndMinPriceDto;
import com.example.scrapper.model.dto.PriceSampleDto;
import com.example.scrapper.model.entity.PriceSampleEntity;
import com.example.scrapper.repository.PriceSampleRepository;
import com.example.scrapper.service.PriceSampleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PriceSampleTest extends BaseTest {
    @Autowired
    private PriceSampleService priceSampleService;
    @Autowired
    private PriceSampleRepository priceSampleRepository;
    @MockBean
    private UralsOilScrapper uralsOilScrapper;

    @BeforeEach
    void init() {
        priceSampleRepository.save(new PriceSampleEntity(
                LocalDate.of(2016, 1, 15),
                LocalDate.of(2016, 2, 15),
                767.5)
        );
        priceSampleRepository.save(new PriceSampleEntity(
                LocalDate.of(2016, 2, 16),
                LocalDate.of(2016, 3, 16),
                528.5)
        );
        priceSampleRepository.save(new PriceSampleEntity(
                LocalDate.of(2016, 3, 17),
                LocalDate.of(2016, 4, 17),
                443.5)
        );
        priceSampleRepository.save(new PriceSampleEntity(
                LocalDate.of(2016, 5, 18),
                LocalDate.of(2016, 5, 18),
                686.5)
        );
    }

    @AfterEach
    void clearDb() {
        priceSampleRepository.deleteAll();
    }

    @Test
    void testGetAllSamples() {
        int actualSamplesAmount = priceSampleService.getAllSamples().size();

        assertThat(actualSamplesAmount).isEqualTo(4);
    }

    @Test
    void testUpdateSamples() {
        Mockito.when(uralsOilScrapper.getAllSamples())
                .thenReturn(
                        List.of(
                                new ExternalPriceSampleDto("15.мар.13", "14.апр.13", "764,6"),
                                new ExternalPriceSampleDto("15.апр.13", "14.май.13", "732,8"),
                                new ExternalPriceSampleDto("15.май.13", "14.июн.13", "749,3")
                        )
                );
        priceSampleService.updateSamples();
        List<PriceSampleDto> actualSamples = priceSampleService.getAllSamples();

        assertThat(actualSamples.size()).isEqualTo(3);
        assertThat(actualSamples).contains(new PriceSampleDto(
                LocalDate.of(2013, 3, 15),
                LocalDate.of(2013, 4, 14),
                764.6));
        assertThat(actualSamples).contains(new PriceSampleDto(
                LocalDate.of(2013, 4, 15),
                LocalDate.of(2013, 5, 14),
                732.8));
        assertThat(actualSamples).contains(new PriceSampleDto(
                LocalDate.of(2013, 5, 15),
                LocalDate.of(2013, 6, 14),
                749.3));
    }

    @Test
    void testGetAveragePriceOfSpecificSampleByDateSuccess() {
        Double averagePrice = priceSampleService.getAveragePriceOfSpecificSampleByDate(LocalDate.of(2016, 3, 5));

        assertThat(averagePrice).isEqualTo(528.5);
    }

    @Test
    void testGetAveragePriceOfSpecificSampleByDateFailure() {
        Exception exception = assertThrows(PriceSampleServiceException.class, () ->
                priceSampleService.getAveragePriceOfSpecificSampleByDate(LocalDate.of(2399, 3, 5))
        );

        assertThat(exception.getMessage()).isEqualTo("Price sample not found");
    }

    @Test
    void testGetAveragePriceOfSpecificSamplesInRangeSuccess() {
        Double averagePrice = priceSampleService.getAveragePriceOfSpecificSamplesInRange(
                new DateRangeDto(
                        LocalDate.of(2016, 2, 16),
                        LocalDate.of(2016, 5, 18)
                )
        );

        assertThat(averagePrice).isEqualTo(552.8333333333334);
    }

    @Test
    void testGetAveragePriceOfSpecificSamplesInRangeInvalidDateFailure() {
        Exception exception = assertThrows(PriceSampleServiceException.class, () ->
                priceSampleService.getAveragePriceOfSpecificSamplesInRange(
                        new DateRangeDto(
                                LocalDate.of(2016, 5, 18),
                                LocalDate.of(2016, 2, 16)
                        ))
        );

        assertThat(exception.getMessage()).isEqualTo("Specified date range is invalid");
    }

    @Test
    void testGetAveragePriceOfSpecificSamplesInRangeNotFoundFailure() {
        Exception exception = assertThrows(PriceSampleServiceException.class, () ->
                priceSampleService.getAveragePriceOfSpecificSamplesInRange(
                        new DateRangeDto(
                                LocalDate.of(2300, 2, 16),
                                LocalDate.of(2300, 5, 18)
                                ))
        );

        assertThat(exception.getMessage()).isEqualTo("Price samples not found");
    }

    @Test
    void testGetMaxAndMinPriceOfSpecificSamplesInRangeSuccess() {
        MaxAndMinPriceDto maxAndMin = priceSampleService.getMaxAndMinPriceOfSpecificSamplesInRange(
                new DateRangeDto(
                        LocalDate.of(2016, 2, 16),
                        LocalDate.of(2016, 5, 18)
                )
        );

        assertThat(maxAndMin.getMax()).isEqualTo(686.5);
        assertThat(maxAndMin.getMin()).isEqualTo(443.5);
    }

    @Test
    void testGetMaxAndMinPriceOfSpecificSamplesInRangeInvalidRangeFailure() {
        Exception exception = assertThrows(PriceSampleServiceException.class, () ->
                priceSampleService.getAveragePriceOfSpecificSamplesInRange(
                        new DateRangeDto(
                                LocalDate.of(2016, 5, 18),
                                LocalDate.of(2016, 2, 16)
                        ))
        );

        assertThat(exception.getMessage()).isEqualTo("Specified date range is invalid");
    }

    @Test
    void testGetMaxAndMinPriceOfSpecificSamplesInRangeNotFoundFailure() {
        Exception exception = assertThrows(PriceSampleServiceException.class, () ->
                priceSampleService.getAveragePriceOfSpecificSamplesInRange(
                        new DateRangeDto(
                                LocalDate.of(2300, 2, 16),
                                LocalDate.of(2300, 5, 18)
                        ))
        );

        assertThat(exception.getMessage()).isEqualTo("Price samples not found");
    }
}
