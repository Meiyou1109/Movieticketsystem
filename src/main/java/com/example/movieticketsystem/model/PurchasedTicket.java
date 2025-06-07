package com.example.movieticketsystem.model;

import java.util.List;

public class PurchasedTicket {
    private String username;
    private String movieTitle;
    private String showtime;
    private List<String> seatLabels;
    private int totalPrice;

    public PurchasedTicket() {}

    public PurchasedTicket(String username, String movieTitle, String showtime, List<String> seatLabels, int totalPrice) {
        this.username = username;
        this.movieTitle = movieTitle;
        this.showtime = showtime;
        this.seatLabels = seatLabels;
        this.totalPrice = totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public List<String> getSeatLabels() {
        return seatLabels;
    }

    public void setSeatLabels(List<String> seatLabels) {
        this.seatLabels = seatLabels;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
