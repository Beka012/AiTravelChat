package com.example.TourService.model;

import java.util.List;

public class TourResponse {
    private String link;
    private String hotelName;
    private List<TourInfo> tourInfo;

    public TourResponse() {}

    public TourResponse(String hotelName, List<TourInfo> tourInfo) {
        this.hotelName = hotelName;
        this.tourInfo = tourInfo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

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
