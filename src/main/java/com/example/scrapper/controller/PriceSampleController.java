package com.example.scrapper.controller;

import com.example.scrapper.model.dto.DateRangeDto;
import com.example.scrapper.model.dto.MaxAndMinPriceDto;
import com.example.scrapper.model.dto.PriceSampleDto;
import com.example.scrapper.service.PriceSampleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/price")
public class PriceSampleController {

    @Autowired
    private PriceSampleService priceSampleService;

    @ApiOperation("Scrap, update and return all price samples")
    @PostMapping("/update")
    public List<PriceSampleDto> updatePriceSamples() {
        priceSampleService.updateSamples();
        return priceSampleService.getAllSamples();
    }

    @ApiOperation("Get all price samples")
    @GetMapping("/all")
    public List<PriceSampleDto> getAllPriceSamples() {
        return priceSampleService.getAllSamples();
    }

    @ApiOperation("Get average price in specific date")
    @ApiResponse(code = 404, message = "Price for specified date does not found")
    @GetMapping("/specific")
    public Double getAveragePriceForSpecificDate(@RequestParam("date")
                                                 @DateTimeFormat(pattern = "dd.MM.yyyy")
                                                 @ApiParam(value = "dd.MM.yyyy")
                                                 LocalDate date) {
        return priceSampleService.getAveragePriceOfSpecificSampleByDate(date);
    }

    @ApiOperation("Get average price in specific date range")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Prices for specified date range are not found"),
            @ApiResponse(code = 400, message = "Date range is invalid")
    })
    @GetMapping("/range")
    public Double getAveragePriceForSpecificRange(DateRangeDto dateRangeDto) {
        return priceSampleService.getAveragePriceOfSpecificSamplesInRange(dateRangeDto);
    }

    @ApiOperation("Get min and max average prices in specific date range")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Prices for specified date range are not found"),
            @ApiResponse(code = 400, message = "Date range is invalid")
    })
    @GetMapping("/minmax")
    public MaxAndMinPriceDto getMaxAndMinAveragePricesForSpecificDateRange(DateRangeDto dateRangeDto) {
        return priceSampleService.getMaxAndMinPriceOfSpecificSamplesInRange(dateRangeDto);
    }
}
