package com.example.TourService.controller;

import com.example.TourService.model.TourRequest;
import com.example.TourService.model.TourResponse;
import com.example.TourService.service.TimeflyTravelParser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {
    TimeflyTravelParser timeflyTravelParser;

    public TourController(TimeflyTravelParser timeflyTravelParser) {
        this.timeflyTravelParser = timeflyTravelParser;
    }

    @PostMapping("/search")
    public List<TourResponse> getTour(@RequestBody TourRequest tourRequest) {
        return timeflyTravelParser.parseTimeflyTravel(tourRequest);
    }
}
