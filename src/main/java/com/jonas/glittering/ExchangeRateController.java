package com.jonas.glittering;

import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;


@RestController
public class ExchangeRateController {

    @Autowired
    ExchangeRateService exchangeRateService;

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> currencies() throws HttpResponseException {
        List<String> exchangeRates = exchangeRateService.getValidExchangeRates().getRates().keySet().stream().toList();
        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    @GetMapping("/convertCurrency")
    public ResponseEntity<String> convertCurrency(@RequestParam String sourceCurrency, @RequestParam String targetCurrency, @RequestParam Double amount) throws HttpResponseException {
        Map<String, Double> rates = exchangeRateService.getValidExchangeRates().getRates();
        Double sourceRate = rates.get(sourceCurrency);
        Double targetRate = rates.get(targetCurrency);

        if (sourceRate == null || targetRate == null) {
            return new ResponseEntity("Invalid currencies!", HttpStatus.BAD_REQUEST);
        }

        double result = exchangeRateService.convertCurrency(amount, sourceRate, targetRate);
        return new ResponseEntity(result, HttpStatus.OK);
    }

}