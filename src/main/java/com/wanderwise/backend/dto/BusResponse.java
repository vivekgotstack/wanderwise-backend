package com.wanderwise.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusResponse {
    private Long id;
    private String operator;
    private String type;
    private String departureTime;
    private int price;
}