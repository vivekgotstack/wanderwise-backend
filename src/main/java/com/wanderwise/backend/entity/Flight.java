package com.wanderwise.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String airline;
    private String source;
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private int totalSeats;
    private int availableSeats;

    private String flightNumber;
    private String aircraft;

    private BigDecimal basePrice;
}