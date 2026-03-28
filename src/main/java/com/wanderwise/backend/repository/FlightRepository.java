package com.wanderwise.backend.repository;

import com.wanderwise.backend.entity.Flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findBySourceAndDestinationAndDepartureTimeBetween(
            String source,
            String destination,
            LocalDateTime start,
            LocalDateTime end
    );
    Page<Flight> findBySourceAndDestinationAndDepartureTimeBetween(
        String source,
        String destination,
        LocalDateTime start,
        LocalDateTime end,
        Pageable pageable
    );
}