package com.enrollmentsystem.config;

import com.enrollmentsystem.repositories.UserRepository;
import com.enrollmentsystem.services.AuthService;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AppContext {
    private static UserRepository userRepository;

    private static AuthService authService;

    private static Connection connection;

    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DatabaseConnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Connection Error: ", e);
            }
        }
        return connection;
    }

    private static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository(getConnection());
        }
        return userRepository;
    }

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(getUserRepository());
        }
        return authService;
    }
}
