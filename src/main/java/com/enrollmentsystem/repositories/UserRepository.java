package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public Integer countUsers(Connection conn) throws SQLException {
        String query = "SELECT COUNT(user_id) FROM users";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : null;
        }
    }

    public List<User> getAllUsers(Connection conn, int offset) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users LIMIT 15 OFFSET ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setLastName(rs.getString("last_name"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setMiddleName(rs.getString("middle_name"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(Role.fromString(rs.getString("role")));
                    user.setStatus(UserStatus.fromString(rs.getString("status")));

                    users.add(user);
                }
            }
        }

        return users;
    }

    public User findByUsername(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.fromString(rs.getString("role")),
                            UserStatus.fromString(rs.getString("status"))
                    );
                }
            }

            return null;
        }
    }

    public void addUser(Connection conn, User user) throws SQLException {
        String query = "INSERT INTO users (last_name, first_name, middle_name, username, password, role, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLastName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRole().getValue());
            statement.setString(7, user.getStatus().getStatus());

            statement.executeUpdate();
        }
    }

    public User findUserById(Connection conn, int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.fromString(rs.getString("role")),
                            UserStatus.fromString(rs.getString("status"))
                    );
                }
            }

            return null;
        }
    }

    public int updateUser(Connection conn, User user) throws SQLException {
        String query = "UPDATE users " +
                        "SET last_name = ?, first_name = ?, middle_name = ?, username = ?, role = ? " +
                        "WHERE user_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getLastName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getRole().getValue());
            statement.setInt(6, user.getId());

            return statement.executeUpdate();
        }
    }

    public int updateUserStatusById(Connection conn, int userId, UserStatus status) throws SQLException {
        String query = "UPDATE users " +
                        "SET status = ? " +
                        "WHERE user_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status.getStatus());
            statement.setInt(2, userId);

            return statement.executeUpdate();
        }
    }

    public int resetUserPassword(Connection conn, int userId, String password) throws SQLException {
        String query = "UPDATE users " +
                        "SET password = ? " +
                        "WHERE user_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, password);
            statement.setInt(2, userId);

            return statement.executeUpdate();
        }
    }

    public int updateUserPassword(Connection conn, String hashedPassword, int userId) throws SQLException {
        String query = "UPDATE users " +
                        "SET password = ? " +
                        "WHERE user_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);

            return statement.executeUpdate();
        }
    }
}
