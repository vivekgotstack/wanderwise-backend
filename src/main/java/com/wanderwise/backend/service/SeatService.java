package com.wanderwise.backend.service;

import com.wanderwise.backend.entity.Seat;
import com.wanderwise.backend.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<Seat> getSeatsByFlight(Long flightId) {
        List<Seat> seats = seatRepository.findByFlightId(flightId);
    
        System.out.println("DEBUG → flightId=" + flightId + ", seats=" + seats.size());
    
        return seats;
    }
}