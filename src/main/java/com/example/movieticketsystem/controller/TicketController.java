package com.example.movieticketsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movieticketsystem.model.Movie;
import com.example.movieticketsystem.model.PurchasedTicket;
import com.example.movieticketsystem.model.Seat;
import com.example.movieticketsystem.model.User;
import com.example.movieticketsystem.repository.PurchasedTicketRepository;
import com.example.movieticketsystem.repository.SeatRepository;
import com.example.movieticketsystem.service.TicketService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TicketController {

    private final SeatRepository seatRepo = new SeatRepository(); // hỗ trợ nhiều phòng
    private final TicketService ticketService = new TicketService();

    @Autowired
    private PurchasedTicketRepository purchasedRepo;

    @GetMapping("/")
    public String index(HttpSession session, Model model,
                        @RequestParam(required = false, defaultValue = "Avengers") String movie,
                        @RequestParam(required = false, defaultValue = "10:30") String showtime) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("seats", seatRepo.getSeats(movie, showtime));
        model.addAttribute("movie", new Movie(movie, "", 100));
        model.addAttribute("tickets", purchasedRepo.findByUser(user.getUsername()));
        model.addAttribute("selectedMovie", movie);
        model.addAttribute("selectedShowtime", showtime);

        return "index";
    }

    @PostMapping("/reserve")
    public String reserveSeats(@RequestParam(value = "seatNumbers", required = false) String seatNumbers,
                               @RequestParam String movieTitle,
                               @RequestParam String showtime,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Seat> selectedSeats = new ArrayList<>();
        List<String> seatLabels = new ArrayList<>();

        if (seatNumbers != null && !seatNumbers.isBlank()) {
            String[] seatIds = seatNumbers.split(",");

            for (String seatId : seatIds) {
                if (seatId.isBlank()) continue;
                int number = Integer.parseInt(seatId);
                Seat seat = seatRepo.getSeat(movieTitle, showtime, number);

                if (!seat.isAvailable()) {
                    model.addAttribute("alert", "❌ Ghế " + getSeatLabel(number) + " đã được mua trước đó. Vui lòng chọn lại.");
                    model.addAttribute("seats", seatRepo.getSeats(movieTitle, showtime));
                    model.addAttribute("movie", new Movie(movieTitle, "", 100));
                    model.addAttribute("tickets", purchasedRepo.findByUser(user.getUsername()));
                    model.addAttribute("selectedMovie", movieTitle);
                    model.addAttribute("selectedShowtime", showtime);
                    return "index";
                }

                selectedSeats.add(seat);
                seatLabels.add(getSeatLabel(number));
            }

            for (Seat seat : selectedSeats) {
                ticketService.reserveSeat(seat);
            }

            int total = (int) (selectedSeats.size() * 100);

            purchasedRepo.save(new PurchasedTicket(
                user.getUsername(),
                movieTitle,
                showtime,
                seatLabels,
                total
            ));

            model.addAttribute("alert", "✅ Đặt vé thành công!");
        }

        model.addAttribute("seats", seatRepo.getSeats(movieTitle, showtime));
        model.addAttribute("movie", new Movie(movieTitle, "", 100));
        model.addAttribute("selectedMovie", movieTitle);
        model.addAttribute("selectedShowtime", showtime);
        model.addAttribute("tickets", purchasedRepo.findByUser(user.getUsername()));

        return "index";
    }

    @PostMapping("/cancel")
    public String cancelTicket(@RequestParam String movie,
                               @RequestParam String showtime,
                               @RequestParam String seats,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<String> seatList = List.of(seats.split(","));
        purchasedRepo.deleteTicket(user.getUsername(), movie, showtime, seatList);

        for (String label : seatList) {
            int seatIndex = getSeatIndex(label);
            Seat seat = seatRepo.getSeat(movie, showtime, seatIndex);
            seat.cancel();
        }

        model.addAttribute("seats", seatRepo.getSeats(movie, showtime));
        model.addAttribute("movie", new Movie(movie, "", 100));
        model.addAttribute("selectedMovie", movie);
        model.addAttribute("selectedShowtime", showtime);
        model.addAttribute("tickets", purchasedRepo.findByUser(user.getUsername()));

        return "index";
    }

    private String getSeatLabel(int seatNumber) {
        int rowIndex = (seatNumber - 1) / 10;
        int colIndex = ((seatNumber - 1) % 10) + 1;
        char rowChar = "ABCDEF".charAt(rowIndex);
        return rowChar + String.valueOf(colIndex);
    }

    private int getSeatIndex(String label) {
        char rowChar = label.charAt(0);
        int rowIndex = "ABCDEF".indexOf(rowChar);
        int col = Integer.parseInt(label.substring(1));
        return rowIndex * 10 + col;
    }
}
