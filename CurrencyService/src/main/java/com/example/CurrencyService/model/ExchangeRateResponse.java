package com.example.CurrencyService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExchangeRateResponse {
    private String result;
    private Map<String,Double> conversion_rates;
}
