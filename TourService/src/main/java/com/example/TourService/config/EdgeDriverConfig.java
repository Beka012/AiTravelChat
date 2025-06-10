package com.example.TourService.config;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EdgeDriverConfig {

    @Bean
    public EdgeDriver edgeDriver() {
        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless"); // Режим без GUI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new EdgeDriver(options);
    }
}
