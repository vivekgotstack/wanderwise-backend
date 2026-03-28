package com.wanderwise.backend.controller;

import com.wanderwise.backend.entity.Flight;
import com.wanderwise.backend.service.FlightService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/search")
    public Page<Flight> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "basePrice") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return flightService.searchFlights(
                source,
                destination,
                LocalDateTime.parse(date),
                page,
                size,
                sortBy,
                direction);
    }
}