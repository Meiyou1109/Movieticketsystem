package com.example.movieticketsystem.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.movieticketsystem.model.Seat;

public class SeatRepository {
    private final Map<String, List<Seat>> seatMap = new HashMap<>();

    public List<Seat> getSeats(String movieTitle, String showtime) {
        String key = generateKey(movieTitle, showtime);
        if (!seatMap.containsKey(key)) {
            seatMap.put(key, createInitialSeats());
        }
        return seatMap.get(key);
    }

    public Seat getSeat(String movieTitle, String showtime, int seatNumber) {
        return getSeats(movieTitle, showtime).get(seatNumber - 1);
    }

    private List<Seat> createInitialSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            seats.add(new Seat(i));
        }
        return seats;
    }

    private String generateKey(String movieTitle, String showtime) {
        return movieTitle + "@" + showtime;
    }
} 