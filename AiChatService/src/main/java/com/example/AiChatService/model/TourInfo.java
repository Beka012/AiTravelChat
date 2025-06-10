package com.example.AiChatService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TourInfo {
    private int star;

    private String departureDate;

    private String numberOfNight;

    private String cityName;

    private String countryName;

    private long price;

    private String meal;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getNumberOfNight() {
        return numberOfNight;
    }

    public void setNumberOfNight(String numberOfNight) {
        this.numberOfNight = numberOfNight;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}
