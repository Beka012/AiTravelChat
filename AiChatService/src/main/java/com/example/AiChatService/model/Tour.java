package com.example.AiChatService.model;

public class Tour {
    private String city;
    private String country;
    private String fromDate;
    private String toDate;
    private int nightFrom;
    private int nightTo;
    private int adult;
    private String meal;
    private int star;

    public Tour() {}

    public Tour(String city, String country, String fromDate, String toDate, int nightFrom, int nightTo, int adult, String meal, int star) {
        this.city = city;
        this.country = country;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.nightFrom = nightFrom;
        this.nightTo = nightTo;
        this.adult = adult;
        this.meal = meal;
        this.star = star;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getNightFrom() {
        return nightFrom;
    }

    public void setNightFrom(int nightFrom) {
        this.nightFrom = nightFrom;
    }

    public int getNightTo() {
        return nightTo;
    }

    public void setNightTo(int nightTo) {
        this.nightTo = nightTo;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
