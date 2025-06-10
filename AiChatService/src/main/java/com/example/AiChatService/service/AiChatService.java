package com.example.AiChatService.service;

import com.example.AiChatService.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiChatService {

    private final TourSearchService tourSearchService;
    private TicketService ticketService;
    private OllamaService ollamaService;
    private final RestTemplate restTemplate;

    public AiChatService(TicketService ticketService, OllamaService ollamaService, TourSearchService tourSearchService) {
        this.ticketService = ticketService;
        this.ollamaService = ollamaService;
        this.tourSearchService = tourSearchService;
        this.restTemplate = new RestTemplate();
    }

    public AiChatResponse<?> getAnswer(String prompt) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            OllamaResponse ollama = ollamaService.getRequest(prompt);

            System.out.println("Ollama response: " + ollama.toString());

            if ("findTicketByTime".equals(ollama.getAction())) {
//                String ticketJson = ticketService.getTicket(ollama);
                try{Thread.sleep(7800);}catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                String ticketJson = "[\n" +
                        "    {\n" +
                        "        \"id\": 0,\n" +
                        "        \"source\": \"FlyArystan\",\n" +
                        "        \"fromCity\": \"ala\",\n" +
                        "        \"toCity\": \"sco\",\n" +
                        "        \"fromDate\": \"2025-06-19T00:45:00\",\n" +
                        "        \"toDate\": \"2025-06-19T04:00:00\",\n" +
                        "        \"currency\": \"KZT\",\n" +
                        "        \"price\": 64260,\n" +
                        "        \"durationMinutes\": 195\n" +
                        "    }\n" +
                        "]";
                List<ParserServiceResponse> ticket = mapper.readValue(ticketJson, new TypeReference<List<ParserServiceResponse>>() {});
                return new AiChatResponse<>("ticket", ticket.get(0));

            } else if ("findTour".equals(ollama.getAction())) {
                TourSearchRequest request = new TourSearchRequest();
                request.setCity(ollama.getTour().getCity());
                request.setCountry(ollama.getTour().getCountry());
                request.setFromDate(ollama.getTour().getFromDate());
                request.setToDate(ollama.getTour().getToDate());
                request.setNightFrom(ollama.getTour().getNightFrom());
                request.setNightTo(ollama.getTour().getNightTo());
                request.setAdult(ollama.getTour().getAdult());
                request.setMeal(ollama.getTour().getMeal());
                request.setStar(ollama.getTour().getStar());

                String responseJson = tourSearchService.searchTours(request);
                List<HotelTour> tours = mapper.readValue(responseJson, new TypeReference<List<HotelTour>>() {});
                return new AiChatResponse<>("tours", tours);

            } else if ("ModelToText".equals(ollama.getAction())) {
                // Обработка нового действия ModelToText
                String textResponse = sendToGenerateService(ollama.getText());
                textResponse = textResponse.replace("Human: ","");
                return new AiChatResponse<>("text", textResponse);

            } else {
                return new AiChatResponse<>("text", "Ваш запрос принят, но билеты не найдены.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new AiChatResponse<>("text", "Произошла ошибка при обработке запроса.");
        }
    }

    private String sendToGenerateService(String message) {
        try {
            String url = "http://139.59.5.206:5000/generate";

            // Создаем тело запроса
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("message", message);

            // Настраиваем заголовки
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создаем HTTP entity
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // Отправляем запрос
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // Извлекаем текст из ответа
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("response")) {
                return responseBody.get("response").toString();
            }

            return "Получен пустой ответ от сервиса генерации";

        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при обращении к сервису генерации: " + e.getMessage();
        }
    }
}