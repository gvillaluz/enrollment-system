package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.BatchAuditDTO;
import com.enrollmentsystem.dtos.BatchRecordDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.models.Strand;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BatchSectioningRepository {
    public BatchAuditDTO getBatchAudit(Connection conn, String schoolYear) throws SQLException {
        String query = "SELECT " +
                        "a.timestamp, " +
                        "CONCAT(u.first_name, ' ', u.last_name) AS processed_by " +
                        "FROM audit_log a " +
                        "LEFT JOIN users u ON a.user_id = u.user_id " +
                        "WHERE a.target_key = ? "+
                        "ORDER BY a.timestamp DESC LIMIT 1";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, schoolYear);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var batchAudit = new BatchAuditDTO();
                    batchAudit.setProcessedBy(rs.getString("processed_by"));
                    batchAudit.setProcessedAt(rs.getObject("timestamp", LocalDateTime.class));

                    return batchAudit;
                } else {
                    return null;
                }
            }
        }
    }

    public List<Strand> getAllStrandIds(Connection conn) throws SQLException {
        String query = "SELECT * FROM shs_strand WHERE is_archived = 0";
        List<Strand> strands = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                var strand = new Strand();
                strand.setStrandId(rs.getInt("shs_strand_id"));
                strand.setStrandCode(rs.getString("strand_code"));
                strand.setStrandDescription(rs.getString("strand_description"));
                strand.setTrackId(rs.getInt("shs_track_id"));
                strand.setArchived(rs.getBoolean("is_archived"));

                strands.add(strand);
            }
        }

        return strands;
    }

    public List<BatchRecordDTO> getAllEnrolledStudents(Connection conn, int schoolYearId) throws SQLException {
        List<BatchRecordDTO> records = new ArrayList<>();
        String query = "SELECT e.*, " +
                        "s.sex " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "WHERE school_year_id = ? AND enrollment_status = 'Enrolled'";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    records.add(new BatchRecordDTO(
                            rs.getInt("enrollment_id"),
                            rs.getString("student_lrn"),
                            Gender.fromString(rs.getString("sex")),
                            rs.getInt("school_year_id"),
                            rs.getInt("grade_level"),
                            Semester.fromString(rs.getString("sem_term")),
                            rs.getInt("shs_track_id"),
                            rs.getInt("shs_strand_id"),
                            rs.getInt("section_id"),
                            EnrollmentStatus.fromString(rs.getString("enrollment_status"))
                    ));
                }
            }
        }

        return records;
    }

    public List<Integer> addSectionsAndGenerateIds(Connection conn, List<Section> sections) throws SQLException {
        List<Integer> generatedIds = new ArrayList<>();

        String query = "INSERT INTO sections (name, school_year_id, shs_strand_id, room_assignment) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (Section section : sections) {
                statement.setString(1, section.getName());
                statement.setInt(2, section.getSchoolYearId());
                statement.setInt(3, section.getStrandId());
                statement.setString(4, section.getRoomAssignment());

                statement.addBatch();
            }

            statement.executeBatch();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    generatedIds.add(rs.getInt(1));
                }
            }
        }

        return generatedIds;
    }

    public void saveUpdatedRecords(Connection conn, List<BatchRecordDTO> recordDTOS) throws SQLException {
        String query = "UPDATE enrollments " +
                        "SET section_id = ? " +
                        "WHERE enrollment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (BatchRecordDTO record : recordDTOS) {
                statement.setInt(1, record.getSectionId());
                statement.setInt(2, record.getEnrollmentId());

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    public void resetEnrollmentSections(Connection conn, int schoolYearId) throws SQLException {
        String query = "UPDATE enrollments SET section_id = NULL WHERE school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            statement.executeUpdate();
        }
    }

    public void deleteCurrentSections(Connection conn, int schoolYearId) throws SQLException {
        String query = "DELETE FROM sections WHERE school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            statement.executeUpdate();
        }
    }
}
