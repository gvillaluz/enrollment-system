package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public Integer countUsers() {
        String query = "SELECT COUNT(user_id) FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : null;
        } catch (SQLException e) {
            System.out.println("Failed to get total rows: " + e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers(int offset) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users LIMIT 15 OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

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
        } catch (SQLException e) {
            System.out.println("Failed to load users: " + e.getMessage());
        }

        return users;
    }

    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query)) {
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
        } catch (SQLException e) {
            System.out.println("Failed to find user: " + e.getMessage());
            return null;
        }
    }

    public Integer addUser(User user) {
        String query = "INSERT INTO users (last_name, first_name, middle_name, username, password, role, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLastName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRole().getValue());
            statement.setString(7, user.getStatus().getStatus());

            statement.executeUpdate();

            try (ResultSet generatedId = statement.getGeneratedKeys()) {
                if (generatedId.next())
                    return generatedId.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Failed to add user: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users " +
                        "SET last_name = ?, first_name = ?, middle_name = ?, username = ?, role = ? " +
                        "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getLastName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getRole().getValue());
            statement.setInt(6, user.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserStatusById(int userId, UserStatus status) {
        String query = "UPDATE users " +
                        "SET status = ? " +
                        "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status.getStatus());
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update user status: " + e.getMessage());
            return false;
        }
    }

    public boolean resetUserPassword(int userId, String password) {
        String query = "UPDATE users " +
                        "SET password = ? " +
                        "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, password);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to reset user password: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserPassword(String hashedPassword, int userId) {
        String query = "UPDATE users " +
                        "SET password = ? " +
                        "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update default password: " + e.getMessage());
            return false;
        }
    }
}
