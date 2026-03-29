package com.wanderwise.backend.controller;

import com.wanderwise.backend.dto.BookingRequest;
import com.wanderwise.backend.entity.Booking;
import com.wanderwise.backend.service.BookingService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @PostMapping("/{id}/confirm")
    public Booking confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    @PostMapping("/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public String deleteBooking(@PathVariable Long bookingId) {
        return bookingService.deleteBooking(bookingId);
    }
}