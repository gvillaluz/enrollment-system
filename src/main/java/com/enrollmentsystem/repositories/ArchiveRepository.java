package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.ArchiveDTO;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.filters.ArchiveFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArchiveRepository {
    public Integer getTotalRows(Connection conn, ArchiveFilter filter) throws SQLException {
        List<Object> params = new ArrayList<>();
        var query = new StringBuilder(
                "SELECT COUNT(e.enrollment_id) FROM enrollments e " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "WHERE e.enrollment_status = 'Archived'"
        );

        if (filter.getGradeLevel() != 0) {
            query.append("AND e.grade_level = ? ");
            params.add(filter.getGradeLevel());
        }

        if (filter.getSchoolYearId() != 0) {
            query.append("AND e.school_year_id = ? ");
            params.add(filter.getSchoolYearId());
        }

        if (filter.getSearchValue() != null && !filter.getSearchValue().isBlank()) {
            query.append("AND (LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) ");
            String search = "%" + filter.getSearchValue() + "%";
            params.add(search);
            params.add(search);
        }

        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        }
    }

    public List<ArchiveDTO> getArchiveEnrollments(Connection conn, ArchiveFilter filter) throws SQLException {
        List<ArchiveDTO> archiveDTOS = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT " +
                        "e.enrollment_id, e.student_lrn, e.section_id, e.school_year_id, e.sem_term, e.shs_track_id, e.shs_strand_id, e.grade_level, e.enrollment_status, e.date_enrolled, e.user_id, " +
                        "s.last_name, s.first_name, s.middle_name, " +
                        "CONCAT(sy.start_year, '-', sy.end_year) AS school_year, " +
                        "t.track_code, " +
                        "st.strand_code " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN school_year sy ON e.school_year_id = sy.school_year_id " +
                        "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                        "LEFT JOIN shs_strand st ON e.shs_strand_id = st.shs_strand_id " +
                        "WHERE e.enrollment_status = 'Archived' "
        );

        if (filter.getGradeLevel() != 0) {
            query.append("AND e.grade_level = ? ");
            params.add(filter.getGradeLevel());
        }

        if (filter.getSchoolYearId() != 0) {
            query.append("AND e.school_year_id = ? ");
            params.add(filter.getSchoolYearId());
        }

        if (filter.getSearchValue() != null && !filter.getSearchValue().isBlank()) {
            query.append("AND (LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) ");
            String search = "%" + filter.getSearchValue() + "%";
            params.add(search);
            params.add(search);
        }

        query.append("ORDER BY e.date_enrolled DESC LIMIT 15 OFFSET ?");
        params.add(filter.getOffset());

        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var dto = new ArchiveDTO(
                            rs.getInt("enrollment_id"),
                            rs.getString("student_lrn"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            rs.getInt("school_year_id"),
                            rs.getString("school_year"),
                            rs.getInt("grade_level"),
                            Semester.fromString(rs.getString("sem_term")),
                            rs.getInt("shs_track_id"),
                            rs.getString("track_code"),
                            rs.getInt("shs_strand_id"),
                            rs.getString("strand_code")
                    );

                    archiveDTOS.add(dto);
                }
            }

            return archiveDTOS;

        }
    }

    public void updateRecordStatus(Connection conn, int archiveId) throws SQLException {
        String query = "UPDATE enrollments " +
                        "SET enrollment_status = 'Enrolled' " +
                        "WHERE enrollment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, archiveId);

            statement.executeUpdate();
        }
    }

    public void deleteRecord(Connection conn, int archiveId) throws SQLException {
        String query = "DELETE FROM enrollments WHERE enrollment_id = ? AND enrollment_status = 'Archived'";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, archiveId);

            statement.executeUpdate();
        }
    }
}
