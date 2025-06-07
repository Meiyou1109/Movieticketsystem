package com.example.movieticketsystem.service;

import java.util.concurrent.locks.ReentrantLock;

import com.example.movieticketsystem.model.Seat;

public class TicketService {

    private final ReentrantLock lock = new ReentrantLock(); // Khóa để đồng bộ

    public boolean reserveSeat(Seat seat) {
    lock.lock();
    try {
        if (seat.isAvailable()) {
            seat.reserve();
            return true;
        }
        return false;
    } finally {
        lock.unlock();
    }
}
}
