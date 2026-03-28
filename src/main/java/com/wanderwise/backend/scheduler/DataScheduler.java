package com.wanderwise.backend.scheduler;

import com.wanderwise.backend.service.DataRefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataScheduler {

    private final DataRefreshService dataRefreshService;

    // Runs 1st day of every month at 2 AM
    @Scheduled(cron = "0 0 2 1 * ?")
    public void runMonthlyRefresh() {
        log.warn("⏰ Monthly job triggered");
        dataRefreshService.refreshData();
    }
}