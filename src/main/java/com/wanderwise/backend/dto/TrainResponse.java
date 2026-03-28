package com.wanderwise.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainResponse {
    private Long id;
    private String trainName;
    private String departureTime;
    private String arrivalTime;
    private String type;
    private int price;
}