package com.wanderwise.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    private Long flightId;
    private Long userId;
    private List<Long> seatIds;
}