package com.example.CurrencyService.controller;

import com.example.CurrencyService.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Double getCurrency(@RequestParam String withCurrency,@RequestParam String toCurrency){
        return currencyService.getRateTo(withCurrency,toCurrency);
    }
}
