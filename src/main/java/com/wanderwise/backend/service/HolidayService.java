package com.wanderwise.backend.service;

import org.springframework.stereotype.Service;

import com.wanderwise.backend.dto.HolidayPackageResponse;

import java.util.List;

@Service
public class HolidayService {

    public List<HolidayPackageResponse> getPackages() {
        return List.of(
                new HolidayPackageResponse(1L, "Goa", 5, 15000),
                new HolidayPackageResponse(2L, "Manali", 6, 18000),
                new HolidayPackageResponse(3L, "Dubai", 4, 45000)
        );
    }
}