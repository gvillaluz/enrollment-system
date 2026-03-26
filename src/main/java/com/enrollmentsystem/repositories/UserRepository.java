package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public User findByUsername(String username) throws SQLException {
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
                            Role.fromString(rs.getString("role"))
                    );
                }
            }
        }

        return null;
    }
}
