package com.wanderwise.backend.service;

import com.wanderwise.backend.entity.Flight;
import com.wanderwise.backend.entity.Seat;
import com.wanderwise.backend.entity.SeatStatus;
import com.wanderwise.backend.repository.FlightRepository;
import com.wanderwise.backend.repository.SeatRepository;
import com.wanderwise.backend.repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataRefreshService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    private final Random random = new Random();

    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

    private final List<String[]> routes = List.of(
            new String[] { "DEL", "MUM" },
            new String[] { "DEL", "BLR" },
            new String[] { "DEL", "HYD" },
            new String[] { "MUM", "BLR" },
            new String[] { "BLR", "HYD" },
            new String[] { "DEL", "CCU" },
            new String[] { "DEL", "MAA" },
            new String[] { "MUM", "GOI" },
            new String[] { "DEL", "LKO" });

    private final List<String[]> airlines = List.of(
            new String[] { "IndiGo", "6E" },
            new String[] { "Air India", "AI" },
            new String[] { "Vistara", "UK" },
            new String[] { "Akasa Air", "QP" });

    public void refreshData() {

        if (running) {
            log.warn("⚠️ Refresh already running");
            return;
        }

        running = true;

        try {
            log.warn("🔥 STARTING MONTHLY DATA REFRESH");

            // bookingRepository.deleteAll();
            // seatRepository.deleteAll();
            // flightRepository.deleteAll();

            int DAYS = 5; // 🔥 SAFE
            int SLOTS = 3; // 🔥 SAFE
            int SEATS = 20; // 🔥 SAFE

            List<Flight> flights = new ArrayList<>();

            for (int day = 0; day < DAYS; day++) {
                LocalDate startDate = flightRepository.findMaxDepartureDate()
                        .map(d -> d.toLocalDate().plusDays(1))
                        .orElse(LocalDate.now());

                LocalDate date = startDate.plusDays(day);

                for (String[] route : routes) {
                    for (String[] airline : airlines) {

                        for (int slot = 0; slot < SLOTS; slot++) {

                            LocalDateTime dep = date.atTime(6 + slot * 3, 0);
                            LocalDateTime arr = dep.plusHours(2 + random.nextInt(2));

                            Flight f = new Flight();
                            f.setAirline(airline[0]);
                            f.setFlightNumber(airline[1] + "-" + (100 + random.nextInt(900)));
                            f.setSource(route[0]);
                            f.setDestination(route[1]);
                            f.setDepartureTime(dep);
                            f.setArrivalTime(arr);
                            f.setTotalSeats(SEATS * 2);
                            f.setAvailableSeats(SEATS * 2);
                            f.setBasePrice(BigDecimal.valueOf(3000 + random.nextInt(3000)));

                            flights.add(f);
                        }
                    }
                }
            }

            // ✅ BATCH SAVE FLIGHTS
            for (int i = 0; i < flights.size(); i += 100) {
                flightRepository.saveAll(
                        flights.subList(i, Math.min(i + 100, flights.size())));
            }

            // ✅ SEATS
            List<Seat> seats = new ArrayList<>();

            for (Flight f : flights) {
                for (int i = 1; i <= SEATS; i++) {

                    Seat s1 = new Seat();
                    s1.setFlight(f);
                    s1.setSeatNumber("A" + i);
                    s1.setStatus(random.nextDouble() < 0.1 ? SeatStatus.BOOKED : SeatStatus.AVAILABLE);

                    Seat s2 = new Seat();
                    s2.setFlight(f);
                    s2.setSeatNumber("B" + i);
                    s2.setStatus(random.nextDouble() < 0.1 ? SeatStatus.BOOKED : SeatStatus.AVAILABLE);

                    seats.add(s1);
                    seats.add(s2);
                }
            }

            // ✅ BATCH SAVE SEATS
            for (int i = 0; i < seats.size(); i += 200) {
                seatRepository.saveAll(
                        seats.subList(i, Math.min(i + 200, seats.size())));
            }

            log.warn("✅ DATA REFRESH COMPLETED");

        } finally {
            running = false;
        }
    }
}