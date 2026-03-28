package com.wanderwise.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.wanderwise.backend.dto.ApiResponse;
import com.wanderwise.backend.dto.BusResponse;
import com.wanderwise.backend.service.BusService;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping("/search")
    public ApiResponse<List<BusResponse>> search(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date
    ) {
        List<BusResponse> buses = busService.searchBuses(source, destination, date);
        return new ApiResponse<>(true, buses, "Buses fetched successfully");
    }
}