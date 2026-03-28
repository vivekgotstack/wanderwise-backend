package com.wanderwise.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HolidayPackageResponse {
    private Long id;
    private String destination;
    private int days;
    private int price;
}