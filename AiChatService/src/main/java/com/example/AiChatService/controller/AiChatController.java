package com.example.AiChatService.controller;

import com.example.AiChatService.model.AiChatResponse;
import com.example.AiChatService.service.AiChatService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/chat")
public class AiChatController {
    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/answer")
    public AiChatResponse<?> getAnswer(@RequestBody String prompt){
        System.out.println(prompt);
        return aiChatService.getAnswer(prompt);
    }
}
