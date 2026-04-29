package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.DashboardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardRepository {
    public DashboardDTO getData(Connection conn, int schoolYearId) throws SQLException {
        String query = "SELECT " +
                "    sy.school_year_id AS school_year_id, " +
                "    CONCAT('S.Y. ', sy.start_year, '-', sy.end_year) AS school_year, " +
                "    COUNT(CASE WHEN e.grade_level = 11 AND e.enrollment_status = 'Enrolled' THEN 1 END) AS total_grade_11, " +
                "    COUNT(CASE WHEN e.grade_level = 12 AND e.enrollment_status = 'Enrolled' THEN 1 END) AS total_grade_12, " +
                "    (SELECT COUNT(*) FROM sections WHERE name LIKE '%11%' AND school_year_id = sy.school_year_id) AS total_grade11_section, " +
                "    (SELECT COUNT(*) FROM sections WHERE name LIKE '%12%' AND school_year_id = sy.school_year_id) AS total_grade12_section, " +
                "    COUNT(CASE WHEN e.grade_level = 11 AND s.sex = 'Male' AND e.enrollment_status = 'Enrolled' THEN 1 END) AS g11_male, " +
                "    COUNT(CASE WHEN e.grade_level = 11 AND s.sex = 'Female' AND e.enrollment_status = 'Enrolled' THEN 1 END) AS g11_female, " +
                "    COUNT(CASE WHEN e.grade_level = 12 AND s.sex = 'Male' AND e.enrollment_status = 'Enrolled' THEN 1 END) AS g12_male, " +
                "    COUNT(CASE WHEN e.grade_level = 12 AND s.sex = 'Female' AND e.enrollment_status = 'Enrolled' THEN 1 END) AS g12_female " +
                "FROM school_year sy " +
                "LEFT JOIN enrollments e ON e.school_year_id = sy.school_year_id " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "WHERE sy.school_year_id = ? " +
                "GROUP BY sy.school_year_id, sy.start_year, sy.end_year";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new DashboardDTO(
                            rs.getInt("school_year_id"),
                            rs.getString("school_year"),
                            rs.getInt("total_grade11_section"),
                            rs.getInt("total_grade12_section"),
                            rs.getInt("total_grade_11"),
                            rs.getInt("total_grade_12"),
                            rs.getInt("g11_male"),
                            rs.getInt("g11_female"),
                            rs.getInt("g12_male"),
                            rs.getInt("g12_female")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
