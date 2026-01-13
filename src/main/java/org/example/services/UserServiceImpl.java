package org.example.services;

import org.example.entities.User;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @Override
    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new BadRequestException("User not Found"));
    }

    @Override
    public User findById(UUID uuid){
        return userRepository.findById(uuid)
                .orElseThrow(()->new ResourceNotFoundException("User Not found"));
    }
}
