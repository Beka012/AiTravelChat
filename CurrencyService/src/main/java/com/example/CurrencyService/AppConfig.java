package com.example.CurrencyService;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;


@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.baseUrl("https://v6.exchangerate-api.com/v6/")
                .build();
    }
}

