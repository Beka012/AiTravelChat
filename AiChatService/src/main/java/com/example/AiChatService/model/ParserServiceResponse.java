package com.example.AiChatService.model;


public class ParserServiceResponse {
    private int id;
    private String source;
    private String fromCity;
    private String toCity;
    private String fromDate;
    private String toDate;
    private String currency;
    private double price;
    private int durationMinutes;

    public ParserServiceResponse() {
    }

    public ParserServiceResponse(int id, String source, String fromCity, String toCity, String fromDate, String toDate, String currency, double price, int dutationMinutes) {
        this.id = id;
        this.source = source;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.currency = currency;
        this.price = price;
        this.durationMinutes = dutationMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "ParserServiceResponse{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", dutationMinutes=" + durationMinutes +
                '}';
    }
}
