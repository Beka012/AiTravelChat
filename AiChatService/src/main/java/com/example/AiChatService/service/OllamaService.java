package com.example.AiChatService.service;

import com.example.AiChatService.model.OllamaFullRequest;
import com.example.AiChatService.model.OllamaFullResponse;
import com.example.AiChatService.model.OllamaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OllamaService {

    private HttpClient client = HttpClient.newHttpClient();
    private String model = "mymodel";

    public OllamaResponse getRequest(String prompt){
        OllamaResponse result = null;
        OllamaFullRequest requestString = new OllamaFullRequest(model,prompt,false);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(requestString);
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create("http://64.227.143.31:11434/api/generate"))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> fullResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(fullResponse.body());
            OllamaFullResponse response = mapper.readValue(fullResponse.body(), OllamaFullResponse.class);
            result = mapper.readValue(response.getResponse(), OllamaResponse.class);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
