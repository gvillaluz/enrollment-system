package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.LoginDTO;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.repositories.UserRepository;
import com.enrollmentsystem.utils.ValidationHelper;

import java.sql.SQLException;
import java.util.Objects;

public class AuthService {
    private static UserRepository _repo;

    public AuthService(UserRepository repo) {
        _repo = repo;
    }

    public User loginUser(LoginDTO loginUser) {
        if (ValidationHelper.isNullOrEmpty(loginUser.getUsername()) || ValidationHelper.isNullOrEmpty(loginUser.getPassword())) {
            throw new RuntimeException("All fields are required.");
        }

        try {
            var user = _repo.findByUsername(loginUser.getUsername());

            if (user == null) {
                throw new RuntimeException("Invalid username or password.");
            }

            if (!Objects.equals(user.getPassword(), loginUser.getPassword())) {
                throw new RuntimeException("Invalid username or password.");
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: ", e);
        }
    }
}
