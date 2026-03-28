package com.wanderwise.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.wanderwise.backend.dto.ApiResponse;
import com.wanderwise.backend.dto.TrainResponse;
import com.wanderwise.backend.service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @GetMapping("/search")
    public ApiResponse<List<TrainResponse>> search(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date
    ) {
        return new ApiResponse<>(true,
                trainService.search(source, destination, date),
                "Trains fetched successfully");
    }
}