package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import com.enrollmentsystem.dtos.EnrollmentDTO;
import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.models.Enrollment;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {
    public Integer getRowsCount(Connection conn, int gradeLevel, int schoolYearId, String searchValue) throws SQLException {
        String query = "SELECT COUNT(e.enrollment_id) FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "WHERE e.grade_level = ? AND e.school_year_id = ? ";

        if (searchValue != null) {
            query += "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE (?))";
            searchValue = "%" + searchValue + "%";
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, gradeLevel);
            statement.setInt(2, schoolYearId);

            if (searchValue != null) {
                statement.setString(3, searchValue);
                statement.setString(4, searchValue);
                statement.setString(5, searchValue);
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }

    public List<EnrollmentDTO> getLatest20(Connection conn, int gradeLevel, int schoolYearId, String searchValue, int offset) throws SQLException {
        List<EnrollmentDTO> enrollmentList = new ArrayList<>();
        String query = "SELECT " +
                        "e.enrollment_id, e.student_lrn, e.section_id, e.school_year_id, e.sem_term, e.shs_track_id, e.shs_strand_id, e.grade_level, e.enrollment_status, e.date_enrolled, e.user_id, " +
                        "s.last_name, s.first_name, s.middle_name, " +
                        "CONCAT(sy.start_year, '-', sy.end_year) AS school_year, " +
                        "t.track_code, " +
                        "st.strand_code, " +
                        "sec.name " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN school_year sy ON e.school_year_id = sy.school_year_id " +
                        "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                        "LEFT JOIN shs_strand st ON e.shs_strand_id = st.shs_strand_id " +
                        "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                        "WHERE e.enrollment_status = 'Enrolled' AND e.grade_level = ? AND e.school_year_id = ? " +
                        (searchValue != null ? "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE (?) OR LOWER(s.first_name) LIKE LOWER(?)) " : "") +
                        "ORDER BY e.date_enrolled DESC LIMIT 17 OFFSET ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
                int paramIndex = 1;
                statement.setInt(paramIndex++, gradeLevel);
                statement.setInt(paramIndex++, schoolYearId);

                if (searchValue != null) {
                    searchValue = "%" + searchValue + "%";

                    statement.setString(paramIndex++, searchValue);
                    statement.setString(paramIndex++, searchValue);
                    statement.setString(paramIndex++, searchValue);
                }

                statement.setInt(paramIndex++, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var enrollment = new EnrollmentDTO(
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
                            rs.getString("strand_code"),
                            rs.getInt("section_id"),
                            rs.getString("name"),
                            EnrollmentStatus.fromString(rs.getString("enrollment_status"))
                    );
                    enrollmentList.add(enrollment);
                }
            }
        }
        return enrollmentList;
    }

    public EnrollmentFormDTO getEnrollmentDataByLRN(Connection conn, String lrn) throws SQLException {
        String query = "SELECT " +
                "s.*, " +
                "a.*, " +
                "e.*," +
                "t.track_code, " +
                "str.strand_code, " +
                "sec.name " +
                "FROM enrollments e " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "LEFT JOIN academic_information a ON e.student_lrn = a.student_lrn " +
                "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                "LEFT JOIN shs_strand str ON e.shs_strand_id = str.shs_strand_id " +
                "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                "WHERE e.student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var enrollmentFormDTO = new EnrollmentFormDTO();
                    enrollmentFormDTO.setSchoolYearId(rs.getInt("school_year_id"));
                    enrollmentFormDTO.setGradeLevel(rs.getInt("grade_level"));
                    enrollmentFormDTO.setEnrollmentId(rs.getInt("enrollment_id"));
                    enrollmentFormDTO.setLrn(rs.getString("student_lrn"));
                    enrollmentFormDTO.setLastName(rs.getString("last_name"));
                    enrollmentFormDTO.setFirstName(rs.getString("first_name"));
                    enrollmentFormDTO.setMiddleName(rs.getString("middle_name"));
                    enrollmentFormDTO.setExtensionName(rs.getString("extension_name"));
                    enrollmentFormDTO.setBirthDate(rs.getDate("birthdate").toLocalDate());
                    enrollmentFormDTO.setAge(rs.getInt("age"));
                    enrollmentFormDTO.setSex(Gender.fromString(rs.getString("sex")));
                    enrollmentFormDTO.setSemester(Semester.fromString(rs.getString("sem_term")));
                    enrollmentFormDTO.setLastGradeLevel(rs.getObject("last_gr_lvl", Integer.class));
                    enrollmentFormDTO.setLastSchoolYear(rs.getString("last_schl_yr"));
                    enrollmentFormDTO.setLastSchoolName(rs.getString("last_schl_name"));
                    enrollmentFormDTO.setLastSchoolId(rs.getString("last_schl_id"));
                    enrollmentFormDTO.setTrackId(rs.getInt("shs_track_id"));
                    enrollmentFormDTO.setTrackCode(rs.getString("track_code"));
                    enrollmentFormDTO.setStrandId(rs.getInt("shs_strand_id"));
                    enrollmentFormDTO.setStrandCode(rs.getString("strand_code"));
                    enrollmentFormDTO.setSectionId(rs.getInt("section_id"));
                    enrollmentFormDTO.setSectionName(rs.getString("name"));

                    return enrollmentFormDTO;
                } else {
                    return null;
                }
            }
        }
    }

    public EnrollmentFormDTO getEnrollmentEditData(Connection conn, int enrollmentId) throws SQLException {
        String query = "SELECT " +
                        "s.*, " +
                        "a.*, " +
                        "e.*," +
                        "t.track_code, " +
                        "str.strand_code, " +
                        "sec.name " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN academic_information a ON e.student_lrn = a.student_lrn " +
                        "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                        "LEFT JOIN shs_strand str ON e.shs_strand_id = str.shs_strand_id " +
                        "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                        "WHERE e.enrollment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, enrollmentId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var enrollmentFormDTO = new EnrollmentFormDTO();
                    enrollmentFormDTO.setSchoolYearId(rs.getInt("school_year_id"));
                    enrollmentFormDTO.setGradeLevel(rs.getInt("grade_level"));
                    enrollmentFormDTO.setEnrollmentId(rs.getInt("enrollment_id"));
                    enrollmentFormDTO.setLrn(rs.getString("student_lrn"));
                    enrollmentFormDTO.setLastName(rs.getString("last_name"));
                    enrollmentFormDTO.setFirstName(rs.getString("first_name"));
                    enrollmentFormDTO.setMiddleName(rs.getString("middle_name"));
                    enrollmentFormDTO.setExtensionName(rs.getString("extension_name"));
                    enrollmentFormDTO.setBirthDate(rs.getDate("birthdate").toLocalDate());
                    enrollmentFormDTO.setAge(rs.getInt("age"));
                    enrollmentFormDTO.setSex(Gender.fromString(rs.getString("sex")));
                    enrollmentFormDTO.setSemester(Semester.fromString(rs.getString("sem_term")));
                    enrollmentFormDTO.setLastGradeLevel(rs.getObject("last_gr_lvl", Integer.class));
                    enrollmentFormDTO.setLastSchoolYear(rs.getString("last_schl_yr"));
                    enrollmentFormDTO.setLastSchoolName(rs.getString("last_schl_name"));
                    enrollmentFormDTO.setLastSchoolId(rs.getString("last_schl_id"));
                    enrollmentFormDTO.setTrackId(rs.getInt("shs_track_id"));
                    enrollmentFormDTO.setTrackCode(rs.getString("track_code"));
                    enrollmentFormDTO.setStrandId(rs.getInt("shs_strand_id"));
                    enrollmentFormDTO.setStrandCode(rs.getString("strand_code"));
                    enrollmentFormDTO.setSectionId(rs.getInt("section_id"));
                    enrollmentFormDTO.setSectionName(rs.getString("name"));

                    return enrollmentFormDTO;
                } else {
                    throw new SQLException("No enrollment records found.");
                }
            }
        }
    }

    public boolean checkEnrollmentExists(Connection conn, String lrn, Semester sem, int schoolYearId) throws SQLException {
        String query = "SELECT * FROM enrollments " +
                        "WHERE student_lrn = ? AND sem_term = ? AND school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);
            statement.setString(2, sem.getSem());
            statement.setInt(3, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addEnrollment(Connection conn, Enrollment enrollment) throws SQLException {
        String query = "INSERT INTO enrollments (student_lrn, section_id, school_year_id, sem_term, shs_track_id, shs_strand_id, grade_level, enrollment_status, date_enrolled, user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, enrollment.getStudentLrn());

            if (enrollment.getSectionId() != null) {
                statement.setInt(2, enrollment.getSectionId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setInt(3, enrollment.getSchoolYearId());
            statement.setString(4, enrollment.getSemTerm().getSem());
            statement.setInt(5, enrollment.getTrackId());
            statement.setInt(6, enrollment.getStrandId());
            statement.setInt(7, enrollment.getGradeLevel());
            statement.setString(8, enrollment.getEnrollmentStatus().getStatus());
            statement.setDate(9, Date.valueOf(enrollment.getDateEnrolled()));
            statement.setInt(10, enrollment.getUserId());

            statement.executeUpdate();
        }
    }

    public void updateEnrollment(Connection conn, Enrollment enrollment) throws SQLException {
        String query = "UPDATE enrollments " +
                        "SET " +
                        "student_lrn = ?, " +
                        "section_id = ?, " +
                        "sem_term = ?, " +
                        "shs_track_id = ?, " +
                        "shs_strand_id = ?, " +
                        "grade_level = ? " +
                        "WHERE enrollment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            System.out.println(enrollment.getStrandId());
            System.out.println(enrollment.getEnrollmentId());

            statement.setString(1, enrollment.getStudentLrn());
            statement.setInt(2, enrollment.getSectionId());
            statement.setString(3, enrollment.getSemTerm().getSem());
            statement.setInt(4, enrollment.getTrackId());
            statement.setInt(5, enrollment.getStrandId());
            statement.setInt(6, enrollment.getGradeLevel());
            statement.setInt(7, enrollment.getEnrollmentId());

            statement.executeUpdate();
        }
    }

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

    public List<EnrollmentDTO> searchEnrollmentByNameOrLRN(int gradeLevel, int schoolYearId, String searchValue) {
        List<EnrollmentDTO> enrollmentDTOS = new ArrayList<>();
        String query = "SELECT " +
                        "e.enrollment_id, e.student_lrn, e.section_id, e.school_year_id, e.sem_term, e.shs_track_id, e.shs_strand_id, e.grade_level, e.enrollment_status, e.date_enrolled, e.user_id, " +
                        "s.last_name, s.first_name, s.middle_name, " +
                        "CONCAT(sy.start_year, '-', sy.end_year) AS school_year, " +
                        "t.track_code, " +
                        "st.strand_code, " +
                        "sec.name " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN school_year sy ON e.school_year_id = sy.school_year_id " +
                        "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                        "LEFT JOIN shs_strand st ON e.shs_strand_id = st.shs_strand_id " +
                        "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                        "WHERE e.enrollment_status = 'Enrolled' AND e.grade_level = ? AND e.school_year_id = ? " +
                        "AND (e.student_lrn = ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) " +
                        "ORDER BY e.date_enrolled DESC LIMIT 20";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            searchValue = "%" + searchValue + "%";

            statement.setInt(1, gradeLevel);
            statement.setInt(2, schoolYearId);
            statement.setString(3, searchValue);
            statement.setString(4, searchValue);
            statement.setString(5, searchValue);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var enrollment = new EnrollmentDTO(
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
                            rs.getString("strand_code"),
                            rs.getInt("section_id"),
                            rs.getString("name"),
                            EnrollmentStatus.fromString(rs.getString("enrollment_status"))
                    );
                    enrollmentDTOS.add(enrollment);
                }
            }

            return enrollmentDTOS;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int archiveEnrollmentByLRN(Connection conn, int enrollmentId) throws SQLException {
        String query = "UPDATE enrollments " +
                        "SET enrollment_status = 'Archived' " +
                        "WHERE enrollment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, enrollmentId);

            return statement.executeUpdate();
        }
    }

    public List<ClasslistRecordDTO> getClasslistRecords(Connection conn, int gradeLevel, int sectionId) throws SQLException {
        List<ClasslistRecordDTO> classlistRecordDTOS = new ArrayList<>();

        String query = "SELECT " +
                        "e.student_lrn, e.grade_level, e.section_id, " +
                        "s.last_name, s.first_name, s.middle_name, " +
                        "sec.name AS section_name " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                        "WHERE e.grade_level = ? AND e.section_id = ? AND e.enrollment_status = 'Enrolled'";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, gradeLevel);
            statement.setInt(2, sectionId);

            try (ResultSet rs = statement.executeQuery()) {
                 while   (rs.next()) {
                     classlistRecordDTOS.add(new ClasslistRecordDTO(
                             rs.getString("student_lrn"),
                             rs.getString("last_name"),
                             rs.getString("first_name"),
                             rs.getString("middle_name"),
                             rs.getInt("grade_level"),
                             rs.getInt("section_id"),
                             rs.getString("section_name")
                     ));
                 }
            }
        }

        return classlistRecordDTOS;
    }
}
