package com.example.petprojectbudgettracker.services;

import com.example.petprojectbudgettracker.models.User;
import com.example.petprojectbudgettracker.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final AuthService authService;
    private final UserRepository userRepository;

    public UserService(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }


    public Double getBalance() {
        User currentUser = authService.getCurrentUser();

        return currentUser.getBudget();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
