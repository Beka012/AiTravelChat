package com.example.AiChatService;

import com.example.AiChatService.service.AiChatService;
import com.example.AiChatService.service.OllamaService;
import com.example.AiChatService.service.TicketService;
import com.example.AiChatService.service.TourSearchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiChatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiChatServiceApplication.class, args);
		TicketService ticketService = new TicketService();
		OllamaService ollamaService = new OllamaService();
		TourSearchService tourSearchService = new TourSearchService();
		AiChatService service = new AiChatService(ticketService, ollamaService,tourSearchService);
	}

}
