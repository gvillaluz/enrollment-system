package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.StudentRecordDTO;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.utils.filters.StudentFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public Integer countRecords(Connection conn, StudentFilter filter, int schoolYearId) throws SQLException {
        List<Object> params = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT COUNT(e.enrollment_id) FROM enrollments e " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "WHERE e.enrollment_status = 'Enrolled' AND e.school_year_id = ? "
        );

        params.add(schoolYearId);

        if (filter.getGradeLevel() > 0) {
            query.append("AND e.grade_level = ? ");
            params.add(filter.getGradeLevel());
        }

        if (filter.getLrn() != null && !filter.getLrn().isBlank()) {
            query.append("AND e.student_lrn = ? ");
            params.add(filter.getLrn());
        }

        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
            query.append("AND LOWER(s.last_name) LIKE LOWER(?) ");
            params.add("%" + filter.getLastName() + "%");
        }

        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            query.append("AND LOWER(s.first_name) LIKE LOWER(?) ");
            params.add("%" + filter.getFirstName() + "%");
        }

        if (filter.getGender() != null) {
            query.append("AND s.sex = ? ");
            params.add(filter.getGender().getGender());
        }

        if (filter.getSemester() != null) {
            query.append("AND e.sem_term = ? ");
            params.add(filter.getSemester().getSem());
        }

        if (filter.getTrackId() > 0) {
            query.append("AND e.shs_track_id = ? ");
            params.add(filter.getTrackId());
        }

        if (filter.getStrandId() > 0) {
            query.append("AND e.shs_strand_id = ? ");
            params.add(filter.getStrandId());
        }

        if (filter.getSectionId() > 0) {
            query.append("AND e.section_id = ? ");
            params.add(filter.getSectionId());
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

    public List<StudentRecordDTO> getLatestStudentRecords(Connection conn, StudentFilter filter, int schoolYearId) throws SQLException {
        List<StudentRecordDTO> records = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT " +
                        "e.enrollment_id, e.student_lrn, e.section_id, e.school_year_id, e.sem_term, e.shs_track_id, e.shs_strand_id, e.grade_level, e.enrollment_status, e.date_enrolled, e.user_id, " +
                        "s.last_name, s.first_name, s.middle_name, s.sex, " +
                        "t.track_code, " +
                        "st.strand_code, " +
                        "sec.name " +
                        "FROM enrollments e " +
                        "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                        "LEFT JOIN shs_track t ON e.shs_track_id = t.shs_track_id " +
                        "LEFT JOIN shs_strand st ON e.shs_strand_id = st.shs_strand_id " +
                        "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                        "WHERE e.enrollment_status = 'Enrolled' AND e.school_year_id = ? "
        );

        params.add(schoolYearId);

        if (filter.getGradeLevel() > 0) {
            query.append("AND e.grade_level = ? ");
            params.add(filter.getGradeLevel());
        }

        if (filter.getLrn() != null && !filter.getLrn().isBlank()) {
            query.append("AND e.student_lrn = ? ");
            params.add(filter.getLrn());
        }

        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
            query.append("AND LOWER(s.last_name) LIKE LOWER(?) ");
            params.add("%" + filter.getLastName() + "%");
        }

        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            query.append("AND LOWER(s.first_name) LIKE LOWER(?) ");
            params.add("%" + filter.getFirstName() + "%");
        }

        if (filter.getGender() != null) {
            query.append("AND s.sex = ? ");
            params.add(filter.getGender().getGender());
        }

        if (filter.getSemester() != null) {
            query.append("AND e.sem_term = ? ");
            params.add(filter.getSemester().getSem());
        }

        if (filter.getTrackId() > 0) {
            query.append("AND e.shs_track_id = ? ");
            params.add(filter.getTrackId());
        }

        if (filter.getStrandId() > 0) {
            query.append("AND e.shs_strand_id = ? ");
            params.add(filter.getStrandId());
        }

        if (filter.getSectionId() > 0) {
            query.append("AND e.section_id = ? ");
            params.add(filter.getSectionId());
        }

        query.append("ORDER BY e.date_enrolled DESC LIMIT 16 OFFSET ?");
        params.add(filter.getOffset());

        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var studentRecordDTO = new StudentRecordDTO(
                            rs.getInt("enrollment_id"),
                            rs.getString("student_lrn"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            Gender.fromString(rs.getString("sex")),
                            rs.getInt("grade_level"),
                            Semester.fromString(rs.getString("sem_term")),
                            rs.getInt("shs_track_id"),
                            rs.getString("track_code"),
                            rs.getInt("shs_strand_id"),
                            rs.getString("strand_code"),
                            rs.getInt("section_id"),
                            rs.getString("name")
                    );
                    records.add(studentRecordDTO);
                }
            }

            return records;
        }
    }

    public boolean checkStudentExistsByLRN(Connection conn, String lrn) throws SQLException {
        String query = "SELECT COUNT(*) FROM student WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Student findStudentByLRN(Connection conn, String lrn) throws SQLException {
        String query = "SELECT * FROM student WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getString("student_lrn"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            rs.getString("extension_name"),
                            rs.getDate("birthdate").toLocalDate(),
                            rs.getInt("age"),
                            Gender.fromString(rs.getString("sex"))
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void addStudent(Connection conn, Student student) throws SQLException {
        String query = "INSERT INTO student (student_lrn, last_name, first_name, middle_name, extension_name, birthdate, age, sex) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, student.getLrn());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getFirstName());
            statement.setString(4, student.getMiddleName());
            statement.setString(5, student.getExtensionName());
            statement.setDate(6, Date.valueOf(student.getBirthDate()));
            statement.setInt(7, student.getAge());
            statement.setString(8, student.getSex().getGender());

            statement.executeUpdate();
        }
    }

    public void updateStudent(Connection conn, Student student) throws SQLException {
        String query = "UPDATE student " +
                        "SET " +
                        "last_name = ?, " +
                        "first_name = ?, " +
                        "middle_name = ?, " +
                        "extension_name = ?, " +
                        "birthdate = ?, " +
                        "age = ?, " +
                        "sex = ? " +
                        "WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, student.getLastName());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getMiddleName());
            statement.setString(4, student.getExtensionName());
            statement.setDate(5, Date.valueOf(student.getBirthDate()));
            statement.setInt(6, student.getAge());
            statement.setString(7, student.getSex().getGender());
            statement.setString(8, student.getLrn());

            statement.executeUpdate();
        }
    }
}
