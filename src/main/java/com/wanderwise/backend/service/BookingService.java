package com.wanderwise.backend.service;

import com.wanderwise.backend.dto.BookingRequest;
import com.wanderwise.backend.entity.*;
import com.wanderwise.backend.repository.BookingRepository;
import com.wanderwise.backend.repository.FlightRepository;
import com.wanderwise.backend.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public Booking createBooking(BookingRequest request) {

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        List<Seat> seats = seatRepository.findByIdInForUpdate(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Some seats do not exist");
        }
        // CRITICAL CHECK
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException(
                        "Seat " + seat.getSeatNumber() + " is not available");
            }
        }

        // LOCK SEATS
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedAt(LocalDateTime.now());
        }

        seatRepository.saveAll(seats);

        // CREATE BOOKING (PENDING)
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        int seatCount = seats.size();
        BigDecimal pricePerSeat = flight.getBasePrice()
                .add(BigDecimal.valueOf(new Random().nextInt(500)));

        BigDecimal totalPrice = pricePerSeat.multiply(BigDecimal.valueOf(seatCount));

        booking.setTotalPrice(totalPrice);

        booking.setSeats(seats);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {

            List<Seat> seats = booking.getSeats();

            for (Seat seat : seats) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedAt(null);
            }

            seatRepository.saveAll(seats);

            booking.setStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);

            throw new RuntimeException("Booking expired and seats released");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking already processed");
        }

        List<Seat> seats = booking.getSeats();

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.LOCKED) {
                throw new RuntimeException("Seat no longer available");
            }
        }

        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedAt(null);
        }

        seatRepository.saveAll(seats);

        Flight flight = booking.getFlight();
        if (flight.getAvailableSeats() < seats.size()) {
            throw new RuntimeException("Not enough seats available");
        }
        int available = (int) seatRepository
                .countByFlightIdAndStatus(flight.getId(), SeatStatus.AVAILABLE);

        flight.setAvailableSeats(available);
        flightRepository.save(flight);
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}