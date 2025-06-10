package org.ticketService.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.ticketService.models.Currency;
import org.ticketService.models.Ticket;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TripParser  extends AbstractTicketParser implements TicketParser{
    private static final Logger log = LoggerFactory.getLogger(TripParser.class);
    public TripParser(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getSourceName() {
        return "Trip Parser";
    }

    @Override
    public List<Ticket> parseTickets(String from, String to, LocalDate date) {
        log.info("Trip parser started");
        log.info("Parsing tickets from " + from + " to " + to);
        Currency currency = Currency.USD;
        LocalDate after3days = date.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        String formattedAfter3Days = after3days.format(formatter);
        String url =
                "https://ru.trip.com/flights/showfarefirst?" +
                        "dcity="+from+"&acity="+to+"&ddate="+formattedDate+"&rdate="+formattedAfter3Days+"&triptype=ow&class=y&lowpricesource=searchform&quantity=1&" +
                        "searchboxarg=t&nonstoponly=off&locale=ru-RU&curr=USD";
        driver.get(url);
        try{Thread.sleep(1000);}catch(InterruptedException e){}

        try{Thread.sleep(5000);}catch(InterruptedException e){}
        List<Ticket> tickets = new ArrayList<>();
        List<String> sourceTicket = source(driver);
        List<LocalTime> fromDateTicket = fromDate(driver);
        List<LocalTime> toDateTicket = toDate(driver);
        List<Integer> durationMinutesTicket = durationMinutes(driver);
        List<Integer> priceTicket = Price(driver);
        for (int i = 0; i < sourceTicket.size(); i++) {
            Ticket ticket = new Ticket(
                    sourceTicket.get(i),
                    from,
                    to,
                    LocalDateTime.of(date, fromDateTicket.get(i)),
                    LocalDateTime.of(date, toDateTicket.get(i)),
                    durationMinutesTicket.get(i),
                    priceTicket.get(i),
                    currency
            );
            tickets.add(ticket);
        }
        return tickets;
    }

    private void clickDirect(WebDriver driver) {
        try{
            driver.findElement(
                    By.xpath("//dl//dd[@class='filter-item']//label[@class='filter-label']")
            ).click();
        }catch(Exception e){
            log.error("Error clicking direct");
        }
    }
    private List<String> source(WebDriver driver)
    {
        log.info("Function source is started");
        try {
            List<String> sources = new ArrayList<>();
            List<WebElement> elements = driver.findElements(
                    By.xpath("//div[@class='flight-name__wrapper']//div[@class='flights-name']"));
            for (WebElement element : elements) sources.add(element.getText());
            return sources;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private List<LocalTime> fromDate(WebDriver driver)
    {
        log.info("Function fromDate is started");
        try {
            List<LocalTime> fromDates = new ArrayList<>();
            List<WebElement> elements = driver.findElements(
                    By.xpath("//div[@class=" +
                            "'flex flight-info-stop__info-wrapper_c5e1 is-departure_2a2b']//div//span//span"));
            for (WebElement element : elements) fromDates.add(LocalTime.parse(element.getText()));
            return fromDates;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private List<LocalTime> toDate(WebDriver driver)
    {
        log.info("Function toDate is started");
        try{
            List<LocalTime> toDates = new ArrayList<>();
            List<WebElement> elements = driver.findElements(
                    By.xpath("//div[@class='flex flight-info-stop__info-wrapper_c5e1 is-arrival_f407']//span[@class='time_cbcc']"));
            for (WebElement element : elements) toDates.add(LocalTime.parse(element.getText()));
            return toDates;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private List<Integer> durationMinutes(WebDriver driver){
        log.info("Function durationMinutes is started");
        try{
            List<Integer> durationMinutes = new ArrayList<>();
            List<WebElement> elements = driver.findElements(
                    By.xpath("//div[@data-testid='flightInfoDuration']")
            );
            for (WebElement element : elements) durationMinutes.add(stringDurationToMinutes(element.getText()));
            return durationMinutes;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    //TODO Это штука не работет почить надо
    //Update работает UwU
    private int stringDurationToMinutes(String duration){
        log.info("Function stringDurationToMinutes is started");
        try{
         Pattern patternHour = Pattern.compile("\\d{1,3}\\D?ч+");
         Pattern digit = Pattern.compile("\\d{1,3}");
         Matcher matcher = patternHour.matcher(duration);
         int minutesResult = 0;
         if(matcher.find()) {
             String hourStr = matcher.group();
             matcher = digit.matcher(hourStr);
             if(matcher.find()) {
                 int hour = Integer.parseInt(matcher.group());
                 minutesResult+=hour*60;
             }
         }
         Pattern patternMinute = Pattern.compile("\\d{1,2}\\D?мин");
         Matcher matcherMinute = patternMinute.matcher(duration);
         if(matcherMinute.find()) {
             String minuteStr = matcherMinute.group();
             matcherMinute = digit.matcher(minuteStr);
             if(matcherMinute.find()) {
                 int minute = Integer.parseInt(matcherMinute.group());
                 minutesResult+=minute;
             }
         }
         return minutesResult;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
    //TODO Это штука не может найти цену на сайте! Надо починить :) EPTA!
    //Update Починил))
    private List<Integer> Price(WebDriver driver)
    {
        log.info("Function Price is started");
        try{
            List<Integer> Prices = new ArrayList<>();
            List<WebElement> elements = driver.findElements(
                    By.xpath("//div[@data-testid='u_price_info']//span"));
            for (WebElement element : elements) Prices.add(onlyPrice(element.getText()));
            return Prices;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    private int onlyPrice(String price){
        log.info("Function onlyPrice is started");
        try{
            int onlyPrice = 0;
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(price);
            if (matcher.find()) {
                String number = matcher.group(1);
                onlyPrice = Integer.parseInt(number);
                System.out.println("Сумма: " + onlyPrice);
            }
            return onlyPrice;
        }catch(Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}
