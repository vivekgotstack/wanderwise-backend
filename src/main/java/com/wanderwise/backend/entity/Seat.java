package com.wanderwise.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@Getter @Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    private LocalDateTime lockedAt;

    private Long bookedBy;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;
}