package com.wanderwise.backend.controller;

import com.wanderwise.backend.service.DataRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataRefreshService dataRefreshService;

    @PostMapping("/refresh")
    public String refresh() {
        new Thread(() -> {
            try {
                dataRefreshService.refreshData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return "Refresh started in background";
    }
}