package com.example.scrapper.component.impl;

import com.example.scrapper.model.dto.ExternalPriceSampleDto;
import com.example.scrapper.component.UralsOilScrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UralsOilScrapperImpl implements UralsOilScrapper {

    private static final String PRICE_TABLE_URL = "https://data.gov.ru/opendata/resource/b6b28991-eaee-44f9-95cb-5f2337c129df#2/0.0/0.0";
    private static final String TABLE_XPATH = "/html/body/div[1]/div/div[3]/div/div/div/div[3]/div/div[7]/div/div/div/span/div/div/div[4]/div[1]/div[5]/div";
    private static final String NEXT_BUTTON_XPATH = "/html/body/div[1]/div/div[3]/div/div/div/div[3]/div/div[7]/div/div/div/span/div/div/div[2]/div[3]/div/ul/li[3]/a";
    private static final int TABLE_PAGES_AMOUNT = 2;
    @Value("${chromedriver.link}")
    private String CHROMEDRIVER_LINK;

    public List<ExternalPriceSampleDto> getAllSamples() {
        WebDriver driver = createDriver();
        List<String> records = cleanRecords(getAllRecords(driver));
        driver.close();
        return extractPriceSamples(records);
    }

    private List<String> cleanRecords(List<String> records) {
        return records.stream()
                .map(record -> record.split("\\r?\\n"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    private List<String> getAllRecords(WebDriver driver) {
        driver.get(PRICE_TABLE_URL);
        List<String> records = new ArrayList<>();
        WebElement table;
        for (int i = 0; i < TABLE_PAGES_AMOUNT; i++) {
            table = driver.findElement(By.xpath(TABLE_XPATH));
            records.addAll(extractRawData(table));
            clickToNextButton(driver);
        }
        return records;
    }

    private void clickToNextButton(WebDriver driver) {
        driver.findElement(By.xpath(NEXT_BUTTON_XPATH)).click();
    }

    private List<String> extractRawData(WebElement table) {
        return table.findElements(By.xpath(".//*")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    private WebDriver createDriver() {
        ChromeOptions opt = new ChromeOptions();
        try {
            return new RemoteWebDriver(new URL(CHROMEDRIVER_LINK), opt);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ExternalPriceSampleDto> extractPriceSamples(List<String> records) {
        List<ExternalPriceSampleDto> result = new ArrayList<>();
        for (int i = 0; i < records.size(); i += 3) {
            result.add(new ExternalPriceSampleDto(records.get(i), records.get(i + 1), records.get(i + 2)));
        }
        return result;
    }
}
