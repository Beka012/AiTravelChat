package com.example.AiChatService.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1) // Используем HTTP/1.1 (Ollama обычно работает по нему)
                .connectTimeout(Duration.ofSeconds(10)) // Сколько ждать *только* на установку соединения
                // Можно добавить .followRedirects(HttpClient.Redirect.NORMAL) если нужно следовать перенаправлениям
                // Можно настроить прокси, аутентификацию и т.д.
                .build(); // Собираем настроенный клиент
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

}
