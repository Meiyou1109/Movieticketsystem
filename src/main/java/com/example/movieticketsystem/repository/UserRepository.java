package com.example.movieticketsystem.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.movieticketsystem.model.User;

@Component
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public void save(User user) {
        users.add(user);
    }

    public boolean exists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public User find(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst().orElse(null);
    }
}
