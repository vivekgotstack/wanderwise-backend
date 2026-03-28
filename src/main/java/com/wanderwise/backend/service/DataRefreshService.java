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
    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

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

    // 🔥 INITIAL SEED (30 DAYS)
    @Transactional
    public void seedInitialData() {

        log.warn("🚀 INITIAL 30 DAY SEED");

        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        flightRepository.deleteAll();

        generateDays(LocalDate.now(), 30);

        log.warn("✅ INITIAL DATA READY");
    }

    // 🔥 DAILY ROLL
    @Transactional
    public void rollOneDay() {

        if (running) {
            log.warn("⚠️ Already running");
            return;
        }

        running = true;

        try {
            log.warn("🔄 ROLLING DATA WINDOW");

            LocalDate oldestDate = flightRepository.findMinDepartureDate()
                    .map(d -> d.toLocalDate())
                    .orElse(LocalDate.now());

            // ❗ delete seats first
            seatRepository.deleteByFlightDepartureDate(oldestDate);

            // then flights
            flightRepository.deleteByDepartureDate(oldestDate);

            LocalDate latestDate = flightRepository.findMaxDepartureDate()
                    .map(d -> d.toLocalDate())
                    .orElse(LocalDate.now());

            LocalDate newDate = latestDate.plusDays(1);

            generateDays(newDate, 1);

            log.warn("✅ ROLL COMPLETE: removed {}, added {}", oldestDate, newDate);

        } finally {
            running = false;
        }
    }

    // 🔥 CORE GENERATOR
    private void generateDays(LocalDate startDate, int days) {

        int SLOTS = 3;
        int SEATS = 20;

        List<Flight> flights = new ArrayList<>();

        for (int day = 0; day < days; day++) {

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

                        int total = SEATS * 2;
                        int booked = random.nextInt(total / 2);

                        f.setTotalSeats(total);
                        f.setAvailableSeats(total - booked);

                        f.setBasePrice(BigDecimal.valueOf(3000 + random.nextInt(3000)));

                        flights.add(f);
                    }
                }
            }
        }

        flightRepository.saveAll(flights);

        // seats
        List<Seat> seats = new ArrayList<>();

        for (Flight f : flights) {

            int bookedSeats = f.getTotalSeats() - f.getAvailableSeats();
            int counter = 0;

            for (int i = 1; i <= SEATS; i++) {

                Seat s1 = new Seat();
                s1.setFlight(f);
                s1.setSeatNumber("A" + i);
                s1.setStatus(counter++ < bookedSeats ? SeatStatus.BOOKED : SeatStatus.AVAILABLE);

                Seat s2 = new Seat();
                s2.setFlight(f);
                s2.setSeatNumber("B" + i);
                s2.setStatus(counter++ < bookedSeats ? SeatStatus.BOOKED : SeatStatus.AVAILABLE);

                seats.add(s1);
                seats.add(s2);
            }
        }

        seatRepository.saveAll(seats);
    }
}