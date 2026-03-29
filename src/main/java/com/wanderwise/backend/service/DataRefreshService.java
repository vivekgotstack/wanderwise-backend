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

    // 🔥 INITIAL SEED → NOW 5 DAYS
    public void seedInitialData() {
        log.warn("🚀 INITIAL 5 DAY SEED");

        deleteAllData();
        generateDays(LocalDate.now(), 5);

        log.warn("✅ INITIAL DATA READY");
    }

    // 🔥 APPEND (UNCHANGED)
    public void appendData(int days) {

        if (running) {
            log.warn("⚠️ Already running");
            return;
        }

        running = true;

        try {
            LocalDate startDate = flightRepository.findMaxDepartureDate()
                    .map(d -> d.toLocalDate().plusDays(1))
                    .orElse(LocalDate.now());

            log.warn("➕ APPENDING {} days from {}", days, startDate);

            generateDays(startDate, days);

            log.warn("✅ APPEND COMPLETE");

        } finally {
            running = false;
        }
    }

    // 🔥 DAILY ROLL (UNCHANGED)
    public void rollOneDay() {

        if (running) {
            log.warn("⚠️ Already running");
            return;
        }

        running = true;

        try {
            LocalDate oldestDate = flightRepository.findMinDepartureDate()
                    .map(d -> d.toLocalDate())
                    .orElse(LocalDate.now());

            deleteDay(oldestDate);

            LocalDate latestDate = flightRepository.findMaxDepartureDate()
                    .map(d -> d.toLocalDate())
                    .orElse(LocalDate.now());

            LocalDate newDate = latestDate.plusDays(1);

            generateDays(newDate, 1);

            log.warn("🔄 WINDOW SHIFT DONE");

        } finally {
            running = false;
        }
    }

    @Transactional
    public void deleteAllData() {
        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        flightRepository.deleteAll();
    }

    @Transactional
    public void deleteDay(LocalDate date) {
        seatRepository.deleteByFlightDepartureDate(date);
        flightRepository.deleteByDepartureDate(date);
    }

    // 🔥 CORE GENERATOR (UPDATED SEAT SIZE ONLY)
    public void generateDays(LocalDate startDate, int days) {

        int SLOTS = 5 + random.nextInt(3); // unchanged (5–7)
        int SEATS = 25 + random.nextInt(6); // 🔥 25–30 rows → 50–60 seats

        List<Flight> flights = new ArrayList<>();

        for (int day = 0; day < days; day++) {

            LocalDate date = startDate.plusDays(day);

            for (String[] route : routes) {
                for (String[] airline : airlines) {

                    for (int slot = 0; slot < SLOTS; slot++) {

                        // SAFE TIME
                        int hour = 6 + slot * 3;
                        hour = hour % 24;
                        if (hour < 6) hour += 6;

                        LocalDateTime dep = date.atTime(hour, 0);
                        LocalDateTime arr = dep.plusHours(2 + random.nextInt(2));

                        Flight f = new Flight();
                        f.setAirline(airline[0]);
                        f.setFlightNumber(airline[1] + "-" + (100 + random.nextInt(900)));
                        f.setSource(route[0]);
                        f.setDestination(route[1]);
                        f.setDepartureTime(dep);
                        f.setArrivalTime(arr);

                        int total = SEATS * 2;

                        // 🔥 MAX 40% booked
                        int booked = (int) (total * (random.nextDouble() * 0.4));

                        f.setTotalSeats(total);
                        f.setAvailableSeats(total - booked);
                        f.setBasePrice(BigDecimal.valueOf(3000 + random.nextInt(4000)));

                        flights.add(f);
                    }
                }
            }
        }

        List<Flight> savedFlights = saveFlights(flights);

        List<Seat> seats = new ArrayList<>();

        for (Flight f : savedFlights) {

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

        saveSeats(seats);
    }

    @Transactional
    public List<Flight> saveFlights(List<Flight> flights) {
        List<Flight> saved = flightRepository.saveAll(flights);
        flightRepository.flush();
        return saved;
    }

    @Transactional
    public void saveSeats(List<Seat> seats) {
        for (int i = 0; i < seats.size(); i += 200) {
            seatRepository.saveAll(
                    seats.subList(i, Math.min(i + 200, seats.size()))
            );
        }
    }
}