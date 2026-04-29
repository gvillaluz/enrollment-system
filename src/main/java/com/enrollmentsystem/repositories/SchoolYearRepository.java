package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.models.SchoolYear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolYearRepository {
    public Integer getActiveSchoolYearId(Connection conn) throws SQLException {
        String query = "SELECT school_year_id FROM school_year WHERE status = 'Open' LIMIT 1";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("school_year_id");
            } else {
                return null;
            }
        }
    }

    public List<SchoolYear> getAllSchoolYears(Connection conn) throws SQLException {
        List<SchoolYear> schoolYears = new ArrayList<>();
        String query = "SELECT * FROM school_year";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery();) {
            while (rs.next()) {
                var sy = new SchoolYear(
                        rs.getInt("school_year_id"),
                        rs.getInt("start_year"),
                        rs.getInt("end_year"),
                        SchoolYearStatus.fromString(rs.getString("status"))
                );
                schoolYears.add(sy);
            }
            return schoolYears;
        }
    }

    public boolean existsByStartYear(Connection conn, int start_year) throws SQLException {
        String query = "SELECT COUNT(*) FROM school_year WHERE start_year = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, start_year);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public SchoolYear getSchoolYearById(Connection conn, int schoolYearId) throws SQLException {
        String query = "SELECT * FROM school_year WHERE school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new SchoolYear(
                        rs.getInt("school_year_id"),
                        rs.getInt("start_year"),
                        rs.getInt("end_year"),
                        SchoolYearStatus.fromString(rs.getString("status"))
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void addSchoolYear(Connection conn, SchoolYear sy) throws SQLException {
        String query = "INSERT INTO school_year (start_year, end_year, status) " +
                        "VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, sy.getStartYear());
            statement.setInt(2, sy.getEndYear());
            statement.setString(3, sy.getStatus().getDbValue());

            statement.executeUpdate();
        }
    }

    public void updateSchoolYear(Connection conn, SchoolYear sy) throws SQLException {
        String query = "UPDATE school_year " +
                        "SET start_year = ?, end_year = ? " +
                        "WHERE school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, sy.getStartYear());
            statement.setInt(2, sy.getEndYear());
            statement.setInt(3, sy.getSchoolYearId());

            statement.executeUpdate();
        }
    }

    public void deactivateAllSchoolYears(Connection conn) throws SQLException {
        String query = "UPDATE school_year " +
                        "SET status = 'Closed' " +
                        "WHERE status = 'Open'";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    public void openSchoolYearById(Connection conn, SchoolYear sy) throws SQLException {
        String query = "UPDATE school_year " +
                        "SET status = 'Open' " +
                        "WHERE school_year_id = ? AND status = 'Closed'";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, sy.getSchoolYearId());

            statement.executeUpdate();
        }
    }
}
