package com.example.scrapper.model.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRangeDto {
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @ApiParam(value = "dd.MM.yyyy")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @ApiParam(value = "dd.MM.yyyy")
    private LocalDate endDate;
}
