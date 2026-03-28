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
import org.springframework.transaction.annotation.Transactional;

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

    private final List<String[]> routes = List.of(
            new String[]{"DEL", "MUM"},
            new String[]{"DEL", "BLR"},
            new String[]{"DEL", "HYD"},
            new String[]{"MUM", "BLR"},
            new String[]{"BLR", "HYD"},
            new String[]{"DEL", "CCU"},
            new String[]{"DEL", "MAA"},
            new String[]{"MUM", "GOI"},
            new String[]{"DEL", "LKO"}
    );

    private final List<String[]> airlines = List.of(
            new String[]{"IndiGo", "6E"},
            new String[]{"Air India", "AI"},
            new String[]{"Vistara", "UK"},
            new String[]{"Akasa Air", "QP"}
    );

    @Transactional
    public void refreshData() {

        log.warn("🔥 STARTING MONTHLY DATA REFRESH");

        // 1. CLEAR OLD DATA
        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        flightRepository.deleteAll();

        // 2. GENERATE NEW DATA (30 DAYS)
        List<Flight> flights = new ArrayList<>();

        for (int day = 0; day < 30; day++) {
            LocalDate date = LocalDate.now().plusDays(day);

            for (String[] route : routes) {
                for (String[] airline : airlines) {

                    for (int slot = 0; slot < 5; slot++) {

                        LocalDateTime dep = date.atTime(6 + slot * 3, 0);
                        LocalDateTime arr = dep.plusHours(2 + random.nextInt(3));

                        Flight f = new Flight();
                        f.setAirline(airline[0]);
                        f.setFlightNumber(airline[1] + "-" + (100 + random.nextInt(900)));
                        f.setSource(route[0]);
                        f.setDestination(route[1]);
                        f.setDepartureTime(dep);
                        f.setArrivalTime(arr);
                        f.setTotalSeats(180);
                        f.setAvailableSeats(180);
                        f.setBasePrice(BigDecimal.valueOf(3000 + random.nextInt(5000)));

                        flights.add(f);
                    }
                }
            }
        }

        flightRepository.saveAll(flights);

        // 3. GENERATE SEATS
        List<Seat> seats = new ArrayList<>();

        for (Flight f : flights) {
            for (int i = 1; i <= 30; i++) {

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

        seatRepository.saveAll(seats);

        log.warn("✅ DATA REFRESH COMPLETED");
    }
}