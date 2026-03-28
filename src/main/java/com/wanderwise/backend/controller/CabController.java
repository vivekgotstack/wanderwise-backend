package com.wanderwise.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.wanderwise.backend.dto.ApiResponse;
import com.wanderwise.backend.dto.CabEstimateResponse;
import com.wanderwise.backend.service.CabService;

import java.util.List;

@RestController
@RequestMapping("/api/cabs")
@RequiredArgsConstructor
public class CabController {

    private final CabService cabService;

    @GetMapping("/estimate")
    public ApiResponse<List<CabEstimateResponse>> estimate(
            @RequestParam String pickup,
            @RequestParam String drop
    ) {
        return new ApiResponse<>(true,
                cabService.estimate(pickup, drop),
                "Fare estimated");
    }
}