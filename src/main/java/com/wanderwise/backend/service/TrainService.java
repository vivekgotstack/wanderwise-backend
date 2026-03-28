package com.wanderwise.backend.service;

import org.springframework.stereotype.Service;

import com.wanderwise.backend.dto.TrainResponse;

import java.util.List;
import java.util.Random;

@Service
public class TrainService {

    public List<TrainResponse> search(String source, String destination, String date) {
        Random random = new Random();

        return List.of(
                new TrainResponse(1L, "Rajdhani Express", "16:00", "08:00", "AC 2 Tier", 1500 + random.nextInt(500)),
                new TrainResponse(2L, "Shatabdi Express", "06:00", "12:00", "Chair Car", 900 + random.nextInt(300)),
                new TrainResponse(3L, "Duronto Express", "22:00", "10:00", "Sleeper", 700 + random.nextInt(200))
        );
    }
}