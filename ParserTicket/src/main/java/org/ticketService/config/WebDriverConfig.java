package org.ticketService.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

    @Bean
    public WebDriver webDriver() {
        WebDriverManager.edgedriver().setup(); // Автоматически скачает нужный драйвер
        return new EdgeDriver();
    }

}
