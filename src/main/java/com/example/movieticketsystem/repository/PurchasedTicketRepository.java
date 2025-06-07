package com.example.movieticketsystem.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.movieticketsystem.model.PurchasedTicket;

@Component
public class PurchasedTicketRepository {
    private final List<PurchasedTicket> tickets = new ArrayList<>();

    public void save(PurchasedTicket ticket) {
        tickets.add(ticket);
    }

    public List<PurchasedTicket> findByUser(String username) {
        return tickets.stream()
                .filter(t -> t.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public void delete(PurchasedTicket target) {
        tickets.removeIf(t ->
                t.getUsername().equals(target.getUsername()) &&
                t.getMovieTitle().equals(target.getMovieTitle()) &&
                t.getShowtime().equals(target.getShowtime()) &&
                t.getSeatLabels().equals(target.getSeatLabels())
        );
    }

    public List<PurchasedTicket> findAll() {
        return new ArrayList<>(tickets);
    }

    public void deleteTicket(String username, String movie, String showtime, List<String> seatLabels) {
        tickets.removeIf(t ->
            t.getUsername().equals(username)
            && t.getMovieTitle().equals(movie)
            && t.getShowtime().equals(showtime)
            && new HashSet<>(t.getSeatLabels()).equals(new HashSet<>(seatLabels))
        );
    }
}
