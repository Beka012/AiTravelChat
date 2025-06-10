package org.ticketService.parsers;

import org.ticketService.models.Ticket;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractTicketParser {

    protected WebDriver driver;
    public AbstractTicketParser(WebDriver driver) {
        this.driver = driver;
    }

    public abstract List<Ticket> parseTickets(String from, String to, LocalDate date);
    public void close(){
        if(driver != null){
            driver.quit();
        }
    }
}
