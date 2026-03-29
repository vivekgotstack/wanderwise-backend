package com.wanderwise.backend.controller;

import com.wanderwise.backend.service.DataRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataRefreshService dataRefreshService;

    // 🔥 INITIAL SEED (RUN ONCE)
    @PostMapping("/seed")
    public String seed() {
        dataRefreshService.seedInitialData();
        return "Initial data seeded";
    }

    // 🔥 DAILY ROLL
    @PostMapping("/roll")
    public String roll() {
        dataRefreshService.rollOneDay();
        return "Rolled one day";
    }
}