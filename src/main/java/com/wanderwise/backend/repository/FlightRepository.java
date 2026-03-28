package com.wanderwise.backend.repository;

import com.wanderwise.backend.entity.Flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

        List<Flight> findBySourceAndDestinationAndDepartureTimeBetween(
                        String source,
                        String destination,
                        LocalDateTime start,
                        LocalDateTime end);

        Page<Flight> findBySourceAndDestinationAndDepartureTimeBetween(
                        String source,
                        String destination,
                        LocalDateTime start,
                        LocalDateTime end,
                        Pageable pageable);

        @Query("SELECT MIN(f.departureTime) FROM Flight f")
        Optional<LocalDateTime> findMinDepartureDate();

        @Query("SELECT MAX(f.departureTime) FROM Flight f")
        Optional<LocalDateTime> findMaxDepartureDate();

        @Modifying
        @Query("DELETE FROM Flight f WHERE DATE(f.departureTime) = :date")
        void deleteByDepartureDate(LocalDate date);
}