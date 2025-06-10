package org.ticketService.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.ticketService.models.Currency;
import org.ticketService.models.Ticket;
import org.springframework.stereotype.Service;
import org.ticketService.parsers.TicketParser;
import org.ticketService.parsers.TicketParserFactory;
import org.ticketService.repositories.TicketRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;
    private final TicketParserFactory ticketParserFactory;
    private final WebClient webClient;

    public TicketService(TicketRepository ticketRepository, TicketParserFactory ticketParserFactory, WebClient webClient) {
        this.ticketRepository = ticketRepository;
        this.ticketParserFactory = ticketParserFactory;
        this.webClient = webClient;
    }

    public List<Ticket> getTickets(String from, String to, LocalDate date) {
        String[] sources = {"Trip Parser"};
        TicketParser parser = ticketParserFactory.getParser(sources[0]);
        if (parser == null) {
            throw new IllegalArgumentException("Unknown source: " + sources[0]);
        }
        List<Ticket> tickets = parser.parseTickets(from, to, date);
        for(Ticket ticket:tickets){
            if(ticket.getCurrency()!=Currency.KZT){
                double price = ticket.getPrice();
                ticket.setPrice(Convertor(price,ticket.getCurrency(),Currency.KZT));
                ticket.setCurrency(Currency.KZT);
            }
        }
        ticketRepository.saveAll(tickets);
        return parser.parseTickets(from, to, date);
    }

    private Double Convertor(double price, Currency withCurrency, Currency toCurrency){
        if(withCurrency.equals(toCurrency)) return 1*price;
        try{
            Double currency = webClient.get()
                    .uri("?withCurrency={withCurrency}&toCurrency={toCurrency}", withCurrency.toString(), toCurrency.toString())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
            if(currency == null) {
                throw new RuntimeException("currency is null");
            }
            return currency*price;
        }catch(Exception ex){
            log.error(ex.getMessage());
            return null;
        }
    }
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}
