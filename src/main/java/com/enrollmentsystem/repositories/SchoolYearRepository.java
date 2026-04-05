package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.models.SchoolYear;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<SchoolYear> getAllSchoolYears() {
        List<SchoolYear> schoolYears = new ArrayList<>();
        String query = "SELECT * FROM school_year";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery();) {
            while (rs.next()) {
                var sy = new SchoolYear(
                        rs.getInt("school_year_id"),
                        rs.getString("start_year"),
                        rs.getString("end_year"),
                        SchoolYearStatus.fromString(rs.getString("status"))
                );
                schoolYears.add(sy);
            }
            return schoolYears;
        } catch (SQLException e) {
            System.out.println("Failed to get school years: " + e.getMessage());
            throw new RuntimeException("Failed to load school years");
        }
    }
}
