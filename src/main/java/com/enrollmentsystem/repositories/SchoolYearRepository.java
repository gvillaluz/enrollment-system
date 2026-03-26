package com.enrollmentsystem.repositories;

import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolYearRepository {
    public Integer getActiveSchoolYearId() {
        String query = "SELECT school_year_id FROM school_year WHERE status = 'Active' LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("school_year_id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get school year id: " + e.getMessage());
            return null;
        }
    }
}
