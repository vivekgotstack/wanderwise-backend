package com.wanderwise.backend.service;

import com.wanderwise.backend.entity.Seat;
import com.wanderwise.backend.entity.SeatStatus;
import com.wanderwise.backend.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatCleanupService {

    private final SeatRepository seatRepository;

    // Runs every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void releaseExpiredSeats() {

        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);

        List<Seat> expiredSeats = seatRepository.findExpiredLockedSeats(expiryTime);

        if (expiredSeats.isEmpty()) return;

        for (Seat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seat.setBookedBy(null);
        }

        seatRepository.saveAll(expiredSeats);

        log.info("Released {} expired seats", expiredSeats.size());
    }
}