package com.example.movieticketsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movieticketsystem.model.Movie;
import com.example.movieticketsystem.model.Seat;
import com.example.movieticketsystem.repository.SeatRepository;
import com.example.movieticketsystem.service.TicketService;

@Controller
public class TicketController {

    private final SeatRepository seatRepo = new SeatRepository(60);
    private final TicketService ticketService = new TicketService();
    private final Movie movie = new Movie("Avengers", "Action", 100);

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("seats", seatRepo.getSeats());
        model.addAttribute("movie", movie);
        return "index";
    }

    @PostMapping("/reserve")
public String reserveSeats(
    @RequestParam(value = "seatNumbers", required = false) String seatNumbers,
    Model model
) {
    int successCount = 0;

    if (seatNumbers != null && !seatNumbers.isBlank()) {
        String[] seatIds = seatNumbers.split(",");

        for (String seatId : seatIds) {
            if (seatId.isBlank()) continue;
            int number = Integer.parseInt(seatId);
            Seat seat = seatRepo.getSeat(number);
            if (seat.isAvailable()) {
                ticketService.reserveSeat(seat);
                successCount++;
            }
        }
    }

    if (successCount > 0) {
    model.addAttribute("message", "Đặt thành công " + successCount + " ghế.");
    }
    model.addAttribute("seats", seatRepo.getSeats());
    model.addAttribute("movie", movie);
    return "index";
}

}
