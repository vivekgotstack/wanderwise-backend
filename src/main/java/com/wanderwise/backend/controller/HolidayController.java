package com.wanderwise.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.wanderwise.backend.dto.ApiResponse;
import com.wanderwise.backend.dto.HolidayPackageResponse;
import com.wanderwise.backend.service.HolidayService;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping
    public ApiResponse<List<HolidayPackageResponse>> getPackages() {
        return new ApiResponse<>(true,
                holidayService.getPackages(),
                "Packages fetched");
    }
}