package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static Connection _conn;

    public UserRepository(Connection connection) {
        _conn = connection;
    }

    public User findByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = _conn.prepareStatement(query)) {
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
                            Role.valueOf(rs.getString("role").toUpperCase())
                    );
                }
            }
        }

        return null;
    }
}
