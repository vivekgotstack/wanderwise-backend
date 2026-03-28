package com.wanderwise.backend.service;

import org.springframework.stereotype.Service;

import com.wanderwise.backend.dto.BusResponse;

import java.util.List;
import java.util.Random;

@Service
public class BusService {

    public List<BusResponse> searchBuses(String source, String destination, String date) {
        Random random = new Random();

        return List.of(
                new BusResponse(1L, "RedBus Travels", "AC Sleeper", "22:00", 800 + random.nextInt(400)),
                new BusResponse(2L, "VRL Travels", "Non-AC Seater", "18:30", 500 + random.nextInt(300)),
                new BusResponse(3L, "Orange Travels", "AC Seater", "20:15", 700 + random.nextInt(350))
        );
    }
}