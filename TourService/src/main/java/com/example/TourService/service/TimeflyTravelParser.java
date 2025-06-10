package com.example.TourService.service;

import com.example.TourService.RussianDateParser;
import com.example.TourService.model.TourInfo;
import com.example.TourService.model.TourRequest;
import com.example.TourService.model.TourResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TimeflyTravelParser {

    final EdgeDriver driver;
    final WebDriverWait wait;
    private String URL = "https://timefly-travel.kz/podbor-tura";

    public TimeflyTravelParser(EdgeDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<TourResponse> parseTimeflyTravel(TourRequest tourRequest) {

        getURL(tourRequest.getNightFrom(), tourRequest.getNightTo(), tourRequest.getFromDate(),tourRequest.getToDate(),tourRequest.getAdult(),tourRequest.getMeal(),tourRequest.getStar());
        openURL();
        pickDepartureCity(tourRequest.getCity());
        pickCountry(tourRequest.getCountry());
        clickFindTour();

        return getTour(tourRequest.getCountry());
    }

    private List<TourResponse> getTour(String country){
        List<TourResponse> tours = new ArrayList<>();

        try{
            Thread.sleep(5000);

            int lastElement = 0;
            int temp=0;
            for (int i = 0; i < 3; i++) {

                List<WebElement> nameOfHotels = driver.findElements(By.xpath("//div[@class=\"TVResultListViewItem\"]" +
                        "//div[@class=\"TVResultItem\"]//div[@class=\"TVResultItemBodyWrapper TVResultItemBodyWrapperStretched\"]" +
                        "//div[@class=\"TVResultItemTitle\"]//a"));

                List<WebElement> nameOfCities = driver.findElements(By.xpath("//div[@class=\"TVResultListViewItem\"]" +
                        "//div[@class=\"TVResultItem\"]//div[@class=\"TVResultItemBodyWrapper TVResultItemBodyWrapperStretched\"]" +
                        "//div[@class=\"TVResultItemSubTitle\"]"));

                List<WebElement> buttonMoreInfo = driver.findElements(By.xpath("//div[@class=\"TVResultItemPriceValueArrow\"]"));

                System.out.println("hotel size " + nameOfHotels.size());
                System.out.println("cities size " + nameOfCities.size());
                System.out.println("button more info " + buttonMoreInfo.size());


                TourResponse tourResponse = new TourResponse();

                List<TourInfo> tourInfos = new ArrayList<>();

                String hotelName = nameOfHotels.get(i).getText();

                buttonMoreInfo.get(i).click();
                Actions actions = new Actions(driver);
                actions.scrollByAmount(0, 200).perform();

                for (int j = 0; j < 3; j++) {
                    Thread.sleep(5000);

                    List<WebElement> dateInfo = driver.findElements(By.xpath("//div[@class=\"TVTourResultItemDate\"]"));

                    List<WebElement> nightInfo = driver.findElements(By.xpath("//div[@class=\"TVTourResultItemNights\"]"));

                    List<WebElement> mealInfo = driver.findElements(By.xpath("//div[@class=\"TVTourResultItemMeal\"]"));

                    List<WebElement> priceInfo = driver.findElements(By.xpath("//div[@class=\"TVTourResultItemPriceValue\"]"));


                    TourInfo tourInfo = new TourInfo.Builder().
                            countryName(country).
                            cityName(nameOfCities.get(i).getText()).
                            start(getStar(hotelName)).
                            departureDate(RussianDateParser.parseRussianDate(dateInfo.get(j+lastElement).getText())).
                            numberOfNight(nightInfo.get(j+lastElement).getText()).
                            food(mealInfo.get(j+lastElement).getText()).
                            price(Integer.parseInt(priceInfo.get(j+lastElement).getText().replace(" ",""))).
                            build();
                    System.out.println(tourInfo.toString());
                    tourInfos.add(tourInfo);
                    temp = priceInfo.size();
                }
                lastElement = temp;
                tourResponse.setHotelName(hotelName);
                tourResponse.setTourInfo(tourInfos);
                tourResponse.setLink(URL);
                tours.add(tourResponse);
            }
        }catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return tours;
    }

    private int getStar(String hotelName){

        System.out.println("Starting getStart");
        Pattern pattern = Pattern.compile("(\\d+)\\*");

            Matcher matcher = pattern.matcher(hotelName);
            if (matcher.find()) {
                System.out.println(matcher.group(1));
                return Integer.parseInt(matcher.group(1));
            }
            return 1;
    }
    private void openURL() {
        driver.get(URL);
    }

    private void pickDepartureCity(String city) {
        WebElement departureCityField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//div[@class=\"TVMainSelect TVAxisDirection-Column TVStyleTheme2 TVTextAlign-Left\"])[1]")
                )
        );
        departureCityField.click();
        List<WebElement> listCity = driver.findElements(
                By.className("TVDepartureTableItemControl")
        );
        for(WebElement element : listCity) {
            if(element.getText().equals(city)) {
                element.click();
                break;
            }
        }
    }
    private void pickCountry(String country) {
        WebElement countryField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//div[@class=\"TVMainSelect TVAxisDirection-Column TVStyleTheme2 TVTextAlign-Left\"])[2]")
                )
        );
        countryField.click();
        List<WebElement> listCountry = driver.findElements(
                By.className("TVComplexListItemContentWrapper")
        );
        for(WebElement element : listCountry) {
            System.out.println(element.getText());
            if(element.getText().equals(country)) {
                System.out.println(element.getText());
                element.click();
                break;
            }
        }
    }
    private void getURL(int nightFrom, int nightTo, LocalDate dateFrom,LocalDate dateTo,int adult,String meal,int star)
    {
        HashMap<String,Integer> mealList = getMealList();
        String url = "https://timefly-travel.kz/podbor-tura?ts_dosearch=1&s_form_mode=0&" +
                "s_nights_from=%d&" +
                "s_nights_to=%d&" +
                "s_regular=1&" +
                "s_j_date_from=%s&" +
                "s_j_date_to=%s&" +
                "s_adults=%d&" +
                "s_stars=%d&" +
                "s_meal=%d&" +
                "s_flyfrom=74&s_country=1&s_currency=3";
        String dateFromString = getDateString(dateFrom);
        String dateToString = getDateString(dateTo);
        URL = String.format(url,nightFrom,nightTo,dateFromString,dateToString,adult,star,mealList.get(meal));
    }

    private String getDateString(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatted = String.format("%s", date.format(formatter));
        System.out.println(formatted);
        return formatted;
    }

    private HashMap<String,Integer> getMealList(){

        HashMap<String,Integer> mealList = new HashMap<>();

        mealList.put("BB",3);
        mealList.put("HB",4);
        mealList.put("FB",5);
        mealList.put("AI",7);
        mealList.put("UAI",9);

        return mealList;
    }

    private void clickFindTour(){
        driver.findElement(
                By.xpath("//*[@id=\"trv-pc\"]/div/div[2]/div/div[1]/div/div[1]/div/div[2]/div[2]/div[3]/div[4]")
        ).click();
    }

}
