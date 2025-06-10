package com.example.AiChatService.service;

import com.example.AiChatService.model.OllamaResponse;
import com.example.AiChatService.model.Ticket;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Format;
import java.util.Formatter;

@Service
public class TicketService {
    HttpClient client = HttpClient.newHttpClient();

    public String getTicket(OllamaResponse response){
        Ticket ticket = response.getTicket();

        String url = String.format("http://localhost:8100/api/tickets?from=%s&to=%s&date=%s",
                ticket.getFrom(),
                ticket.getTo(),
                ticket.getData());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> responseService = client.send(request, HttpResponse.BodyHandlers.ofString());
            return responseService.body();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
