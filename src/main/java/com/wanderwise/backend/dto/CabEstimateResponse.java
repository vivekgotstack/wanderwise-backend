package com.wanderwise.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CabEstimateResponse {
    private String type;
    private double distanceKm;
    private int price;
    private int etaMinutes;
}