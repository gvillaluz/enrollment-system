package com.enrollmentsystem.models;

import com.enrollmentsystem.dtos.UserDTO;

public class UserSession {
    private static UserSession instance;
    private UserDTO user;

    private UserSession(UserDTO user) {
        this.user = user;
    }

    public static void login(UserDTO user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public boolean isAdmin() {
        return user != null && user.getRole().getValue().equals("Admin");
    }

    public static void logout() {
        instance = null;
    }

    public UserDTO getUser() { return user; }
}
