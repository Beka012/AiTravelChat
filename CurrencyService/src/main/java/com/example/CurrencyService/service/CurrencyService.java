package com.example.CurrencyService.service;

import com.example.CurrencyService.ApiKeyProvider;
import com.example.CurrencyService.model.CurrencyResponse;
import com.example.CurrencyService.model.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Service
public class CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final WebClient webClient;
    private final String apiKey = ApiKeyProvider.getApiKey();
    private final RedisTemplate<String,Double> redisTemplate;

    public CurrencyService(WebClient webClient, RedisTemplate<String, Double> redisTemplate) {
        this.webClient = webClient;
        this.redisTemplate = redisTemplate;
    }

    public Double getRateTo(String withCurrency, String toCurrency){
        String cacheKey = withCurrency+"_TO_"+toCurrency;
        Double cachedRate = redisTemplate.opsForValue().get(cacheKey);

        if (cachedRate != null) {
            logger.info("REDIS!");
            logger.info("Rate from Redis: 1 {} = {} {}", withCurrency, cachedRate, toCurrency);
            return cachedRate;
        }
        try{
            ExchangeRateResponse response = webClient.get()
                    .uri("{apiKey}/latest/{withCurrency}",apiKey,withCurrency)
                    .retrieve()
                    .bodyToMono(ExchangeRateResponse.class)
                    .block();

            if(response == null || response.getConversion_rates() == null){
                throw new RuntimeException("Invalid response from currency API");
            }

            Double rate = response.getConversion_rates().get(toCurrency);

            if(rate == null){
                throw new IllegalArgumentException("Unsupported currency: " + toCurrency);
            }

            redisTemplate.opsForValue().set(cacheKey,rate, Duration.ofHours(12));
            logger.info("API!");
            logger.info("1{} = {} {}",withCurrency,rate,toCurrency);


            return rate;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new RuntimeException("Currency service error",ex);
        }
    }
}
