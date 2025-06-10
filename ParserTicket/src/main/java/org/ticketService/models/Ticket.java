package org.ticketService.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tickets")
@NoArgsConstructor
@Setter
@Getter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String source;
    private String fromCity;
    private String toCity;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int DurationMinutes;
    private double Price;
    private Currency currency;


    public Ticket(String source, String fromCity, String toCity, LocalDateTime fromDate, LocalDateTime toDate, int durationMinutes, int price, Currency currency) {
        this.source = source;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.fromDate = fromDate;
        this.toDate = toDate;
        DurationMinutes = durationMinutes;
        Price = price;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", from='" + fromCity + '\'' +
                ", to='" + toCity + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", DurationMinutes=" + DurationMinutes +
                ", Price=" + Price +
                ", currency=" + currency +
                '}';
    }
}
