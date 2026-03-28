package com.wanderwise.backend.service;

import com.wanderwise.backend.entity.Flight;
import com.wanderwise.backend.repository.FlightRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> searchFlights(String source, String destination, LocalDateTime date) {

        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return flightRepository.findBySourceAndDestinationAndDepartureTimeBetween(
                source, destination, startOfDay, endOfDay
        );
    }
    public Page<Flight> searchFlights(
    String source,
    String destination,
    LocalDateTime date,
    int page,
    int size,
    String sortBy,
    String direction
) {

    LocalDateTime start = date.toLocalDate().atStartOfDay();
    LocalDateTime end = start.plusDays(1);

    Sort sort = direction.equalsIgnoreCase("desc")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    return flightRepository
        .findBySourceAndDestinationAndDepartureTimeBetween(
            source, destination, start, end, pageable
        );
}
}