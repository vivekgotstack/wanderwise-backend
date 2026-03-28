package com.wanderwise.backend.controller;

import com.wanderwise.backend.entity.Seat;
import com.wanderwise.backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeats(@PathVariable Long flightId) {
        return seatService.getSeatsByFlight(flightId);
    }
}