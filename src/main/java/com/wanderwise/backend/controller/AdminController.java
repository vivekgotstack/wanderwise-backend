package com.wanderwise.backend.controller;

import com.wanderwise.backend.service.DataRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataRefreshService dataRefreshService;

    @PostMapping("/seed")
    public String seed() {
        new Thread(() -> dataRefreshService.seedInitialData()).start();
        return "Seeding started";
    }

    @PostMapping("/roll")
    public String roll() {
        new Thread(() -> dataRefreshService.rollOneDay()).start();
        return "Rolling started";
    }
}