package com.example.TourService.model;

import java.time.LocalDate;

public class TourInfo {
    private final int star;
    private final LocalDate departureDate;
    private final String numberOfNight;
    private final String cityName;
    private final String countryName;
    private final int price;
    private final String meal;

    private TourInfo(Builder builder) {
        this.star = builder.star;
        this.departureDate = builder.departureDate;
        this.numberOfNight = builder.numberOfNight;
        this.cityName = builder.cityName;
        this.countryName = builder.countryName;
        this.price = builder.price;
        this.meal = builder.food;
    }

    public int getStar() {
        return star;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public String getNumberOfNight() {
        return numberOfNight;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getPrice() {
        return price;
    }

    public String getMeal() {
        return meal;
    }

    @Override
    public String toString() {
        return "TourInfo{" +
                "star=" + star +
                ", departureDate=" + departureDate +
                ", numberOfNight='" + numberOfNight + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", price=" + price +
                ", meal='" + meal + '\'' +
                '}';
    }

    public static class Builder{
        private int star;
        private LocalDate departureDate;
        private String numberOfNight;
        private String cityName;
        private String countryName;
        private int price;
        private String food;

        public Builder start(int star){
            this.star = star;
            return this;
        }

        public Builder departureDate(LocalDate departureDate){
            this.departureDate = departureDate;
            return this;
        }
        public Builder numberOfNight(String numberOfNight){
            this.numberOfNight = numberOfNight;
            return this;
        }
        public Builder cityName(String cityName){
            this.cityName = cityName;
            return this;
        }
        public Builder countryName(String countryName){
            this.countryName = countryName;
            return this;
        }

        public Builder price(int price){
            this.price = price;
            return this;
        }

        public Builder food(String food){
            this.food = food;
            return this;
        }

        public TourInfo build(){
            return new TourInfo(this);
        }
    }
}
