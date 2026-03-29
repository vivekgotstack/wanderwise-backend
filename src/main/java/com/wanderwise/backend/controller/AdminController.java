package com.wanderwise.backend.controller;

import com.wanderwise.backend.service.DataRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataRefreshService dataRefreshService;

    // 🔥 initial seed
    @PostMapping("/seed")
    public String seed() {
        dataRefreshService.seedInitialData();
        return "Seeded initial data (3 days)";
    }

    // 🔥 append more data (KEY)
    @PostMapping("/append")
    public String append() {
        dataRefreshService.appendData(2); // add 2 days each call
        return "Appended 2 more days";
    }

    // 🔥 roll window
    @PostMapping("/roll")
    public String roll() {
        dataRefreshService.rollOneDay();
        return "Rolled one day";
    }
}