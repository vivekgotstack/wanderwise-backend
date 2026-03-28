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

    // every day 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void runDailyRoll() {
        log.warn("⏰ DAILY ROLL TRIGGERED");
        dataRefreshService.rollOneDay();
    }
}