package com.example.AiChatService.service;
import com.example.AiChatService.model.TourSearchRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TourSearchService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchTours(TourSearchRequest request) {
        String url = "http://localhost:9123/api/tour/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TourSearchRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }
}

