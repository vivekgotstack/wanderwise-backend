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
        dataRefreshService.refreshData();
        return "Data refreshed successfully";
    }
}