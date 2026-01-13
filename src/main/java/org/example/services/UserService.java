package org.example.services;

import org.example.entities.User;

import java.util.UUID;

public interface UserService {
    User register(User user);

    User findByEmail(String email);
    User findById(UUID uuid);
}
