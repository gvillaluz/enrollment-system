package com.enrollmentsystem.models;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void login(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void logout() {
        instance = null;
    }

    public User getUser() { return user; }
}
