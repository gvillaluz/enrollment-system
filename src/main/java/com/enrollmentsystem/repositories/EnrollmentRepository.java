package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.EnrollmentDTO;
import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {
    private static Connection _conn;

    public EnrollmentRepository(Connection connection) { _conn = connection; }

    public List<EnrollmentSummaryDTO> getAllSummaries() {
        try {
            List<EnrollmentSummaryDTO> summary = new ArrayList<>();
            String query = "";

            try (PreparedStatement statement = _conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    var enrollmentSummary = new EnrollmentSummaryDTO(
                            rs.getString("LRN"),
                            rs.getString("lastName"),
                            rs.getString("firstName"),
                            rs.getString("middleName"),
                            rs.getString("academicYear"),
                            rs.getInt("grade"),
                            rs.getString("term"),
                            rs.getString("track"),
                            rs.getString("strand"),
                            rs.getString("section"),
                            EnrollmentStatus.fromString(rs.getString("status"))
                    );

                    summary.add(enrollmentSummary);
                }
            }
            return summary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
