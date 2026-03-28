package com.wanderwise.backend.service;

import org.springframework.stereotype.Service;
import com.wanderwise.backend.dto.CabEstimateResponse;

import java.util.List;
import java.util.Random;

@Service
public class CabService {

    public List<CabEstimateResponse> estimate(String pickup, String drop) {
        Random random = new Random();

        double distance = 5 + random.nextInt(15);

        return List.of(
                new CabEstimateResponse("Moto", distance, (int)(distance * 8), 3 + random.nextInt(3)),
                new CabEstimateResponse("Mini", distance, (int)(distance * 12), 4 + random.nextInt(4)),
                new CabEstimateResponse("Sedan", distance, (int)(distance * 15), 5 + random.nextInt(4)),
                new CabEstimateResponse("SUV", distance, (int)(distance * 20), 6 + random.nextInt(5)),
                new CabEstimateResponse("XL", distance, (int)(distance * 18), 6 + random.nextInt(5)),
                new CabEstimateResponse("Premium", distance, (int)(distance * 28), 7 + random.nextInt(6))
        );
    }
}