package org.ticketService.controllers;

import org.ticketService.Service.TicketService;
import org.ticketService.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

//Тестанул говно,надо фиксить((
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<Ticket> getAllTickets(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ticketService.getTickets(from, to, date);
    }

    @GetMapping("/all")
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }
}
