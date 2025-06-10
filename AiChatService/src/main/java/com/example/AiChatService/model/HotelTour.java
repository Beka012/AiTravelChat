package com.example.AiChatService.model;

import java.util.List;

public class HotelTour {
    private String link;
    private String hotelName;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private List<TourInfo> tourInfo;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public List<TourInfo> getTourInfo() {
        return tourInfo;
    }

    public void setTourInfo(List<TourInfo> tourInfo) {
        this.tourInfo = tourInfo;
    }
}
