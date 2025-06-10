package org.ticketService.parsers;

import org.ticketService.models.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketParser {
    List<Ticket> parseTickets(String from, String to, LocalDate date);
    String getSourceName();
}
