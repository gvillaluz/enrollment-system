package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.RequirementNoteDTO;
import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.models.RequirementNote;
import com.enrollmentsystem.models.StudentRequirement;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequirementRepository {
    public Integer getRequirementRowCount(Connection conn, int schoolYearId, String searchValue) throws SQLException {
        String query = "SELECT COUNT(DISTINCT e.student_lrn) FROM enrollments e " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "WHERE e.school_year_id = ? ";

        if (searchValue != null) {
            query += "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE (?))";
            searchValue = "%" + searchValue + "%";
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            if (searchValue != null) {
                statement.setString(2, searchValue);
                statement.setString(3, searchValue);
                statement.setString(4, searchValue);
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }

        }
    }

    public List<RequirementSummaryDTO> getAllChecklist(Connection conn, int schoolYearId, String searchValue, int offset) throws SQLException {
        List<RequirementSummaryDTO> checklist = new ArrayList<>();
        String query = "SELECT " +
                "    sr.student_requirement_id, " +
                "    s.student_lrn, " +
                "    s.last_name, " +
                "    s.first_name, " +
                "    s.middle_name, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 1 THEN sr.is_submitted ELSE 0 END) AS beef, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 2 THEN sr.is_submitted ELSE 0 END) AS sf9, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 3 THEN sr.is_submitted ELSE 0 END) AS psa, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 4 THEN sr.is_submitted ELSE 0 END) AS gmc, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 5 THEN sr.is_submitted ELSE 0 END) AS au, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 6 THEN sr.is_submitted ELSE 0 END) AS forms, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 7 THEN sr.is_submitted ELSE 0 END) AS als_coc, " +
                "   (SELECT COUNT(*) FROM requirement_notes rn WHERE s.student_lrn = rn.student_lrn AND rn.is_resolved = 0) AS note_count " +
                "FROM student s " +
                "LEFT JOIN student_requirements sr ON s.student_lrn = sr.student_lrn " +
                "LEFT JOIN enrollments e ON s.student_lrn = e.student_lrn " +
                "WHERE e.enrollment_status = 'Enrolled' AND e.school_year_id = ? " +
                (searchValue != null ? "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) " : "") +
                "GROUP BY s.student_lrn, s.last_name, s.first_name, s.middle_name " +
                "LIMIT 13 OFFSET ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {

            int paramIndex = 1;
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
                    var requirementDto = new RequirementSummaryDTO();
                    requirementDto.setStudentRequirementId(rs.getInt("student_requirement_id"));
                    requirementDto.setLrn(rs.getString("student_lrn"));
                    requirementDto.setLastName(rs.getString("last_name"));
                    requirementDto.setFirstName(rs.getString("first_name"));
                    requirementDto.setMiddleName(rs.getString("middle_name"));
                    requirementDto.setBeef(rs.getBoolean("beef"));
                    requirementDto.setSf9(rs.getBoolean("sf9"));
                    requirementDto.setPsa(rs.getBoolean("psa"));
                    requirementDto.setGmc(rs.getBoolean("gmc"));
                    requirementDto.setAu(rs.getBoolean("au"));
                    requirementDto.setForm5(rs.getBoolean("forms"));
                    requirementDto.setAlsCol(rs.getBoolean("als_coc"));
                    requirementDto.setNoteCount(rs.getInt("note_count"));

                    checklist.add(requirementDto);
                }
            }

            return checklist;
        }
    }

    public List<RequirementNoteDTO> getRequirementNotesByLRN(Connection conn, String lrn) throws SQLException {
        List<RequirementNoteDTO> notes = new ArrayList<>();

        String query = "SELECT rn.requirement_note_id, rn.note, rn.is_resolved, rn.student_lrn, rn.user_id, rn.date_added, rn.updated_at, " +
                        "u.username, u.role " +
                        "FROM requirement_notes rn " +
                        "LEFT JOIN users u ON rn.user_id = u.user_id " +
                        "WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var note = new RequirementNoteDTO();
                    note.setRequirementNoteId(rs.getInt("requirement_note_id"));
                    note.setNote(rs.getString("note"));
                    note.setResolved(rs.getBoolean("is_resolved"));
                    note.setLrn(rs.getString("student_lrn"));
                    note.setUserId(rs.getInt("user_id"));
                    note.setUsername(rs.getString("username"));
                    note.setUserRole(Role.fromString(rs.getString("role")));
                    note.setDateAdded(rs.getObject("date_added", LocalDateTime.class));
                    note.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

                    notes.add(note);
                }
            }

            return notes;
        }
    }

    public List<StudentRequirement> getStudentRequirementsByLRN(Connection conn, String lrn) throws SQLException {
        List<StudentRequirement> requirements = new ArrayList<>();
        String query = "SELECT * FROM student_requirements WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs  = statement.executeQuery()) {
                while (rs.next()) {
                    var req = new StudentRequirement();
                    req.setStudentRequirementId(rs.getInt("student_requirement_id"));
                    req.setStudentLrn(rs.getString("student_lrn"));
                    req.setRequirementReferenceId(rs.getInt("requirement_ref_id"));
                    req.setSubmitted(rs.getBoolean("is_submitted"));
                    Date date = rs.getDate("date_submitted");
                    req.setDateSubmitted(date != null ? date.toLocalDate() : null);

                    requirements.add(req);
                }
            }

            return requirements;
        }
    }

    public void addStudentRequirements(Connection conn, List<StudentRequirement> requirements) throws SQLException {
        String query = "INSERT INTO student_requirements (student_lrn, requirement_ref_id, is_submitted, date_submitted) " +
                        "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (StudentRequirement req : requirements) {
                statement.setString(1, req.getStudentLrn());
                statement.setInt(2, req.getRequirementReferenceId());
                statement.setBoolean(3, req.isSubmitted());

                if (req.getDateSubmitted() != null) {
                    statement.setDate(4, Date.valueOf(req.getDateSubmitted()));
                } else {
                    statement.setNull(4, Types.DATE);
                }

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    public void updateStudentRequirements(Connection conn, List<StudentRequirement> requirements) throws SQLException {
        String query = "UPDATE student_requirements " +
                        "SET is_submitted = ? " +
                        "WHERE student_requirement_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (StudentRequirement req : requirements) {
                statement.setBoolean(1, req.isSubmitted());
                statement.setInt(2, req.getStudentRequirementId());

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    public int updateRequirement(Connection conn, String lrn, int refId, boolean isSubmitted) throws SQLException {
        String query = "UPDATE student_requirements " +
                        "SET is_submitted = ? " +
                        "WHERE student_lrn = ? AND requirement_ref_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBoolean(1, isSubmitted);
            statement.setString(2, lrn);
            statement.setInt(3, refId);

            return statement.executeUpdate();
        }
    }

    public List<RequirementSummaryDTO> searchRequirementsByStudentName(String name, int schoolYearId) {
        List<RequirementSummaryDTO> requirements = new ArrayList<>();
        String query = "SELECT " +
                "    MAX(sr.student_requirement_id) AS student_requirement_id, " +
                "    s.student_lrn, " +
                "    s.last_name, " +
                "    s.first_name, " +
                "    s.middle_name, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 1 THEN sr.is_submitted ELSE 0 END) AS beef, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 2 THEN sr.is_submitted ELSE 0 END) AS sf9, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 3 THEN sr.is_submitted ELSE 0 END) AS psa, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 4 THEN sr.is_submitted ELSE 0 END) AS gmc, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 5 THEN sr.is_submitted ELSE 0 END) AS au, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 6 THEN sr.is_submitted ELSE 0 END) AS forms, " +
                "    MAX(CASE WHEN sr.requirement_ref_id = 7 THEN sr.is_submitted ELSE 0 END) AS als_coc " +
                "FROM student s " +
                "LEFT JOIN student_requirements sr ON s.student_lrn = sr.student_lrn " +
                "LEFT JOIN enrollments e ON s.student_lrn = e.student_lrn " +
                "WHERE e.enrollment_status = 'Enrolled' AND e.school_year_id = ? " +
                "AND s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) " +
                "GROUP BY s.student_lrn, s.last_name, s.first_name, s.middle_name;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            String searchTerm = "%" + name.trim() + "%";

            statement.setInt(1, schoolYearId);
            statement.setString(2, searchTerm);
            statement.setString(3, searchTerm);
            statement.setString(4, searchTerm);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var requirementDto = new RequirementSummaryDTO();
                    requirementDto.setStudentRequirementId(rs.getInt("student_requirement_id"));
                    requirementDto.setLrn(rs.getString("student_lrn"));
                    requirementDto.setLastName(rs.getString("last_name"));
                    requirementDto.setFirstName(rs.getString("first_name"));
                    requirementDto.setMiddleName(rs.getString("middle_name"));
                    requirementDto.setBeef(rs.getBoolean("beef"));
                    requirementDto.setSf9(rs.getBoolean("sf9"));
                    requirementDto.setPsa(rs.getBoolean("psa"));
                    requirementDto.setGmc(rs.getBoolean("gmc"));
                    requirementDto.setAu(rs.getBoolean("au"));
                    requirementDto.setForm5(rs.getBoolean("forms"));
                    requirementDto.setAlsCol(rs.getBoolean("als_coc"));

                    requirements.add(requirementDto);
                }
            }
            return requirements;
        } catch (SQLException e) {
            System.out.println("Failed to load requirements by name: " + e.getMessage());
            return null;
        }
    }

    public int addRequirementNote(Connection conn, RequirementNote note) throws SQLException {
        String query = "INSERT INTO requirement_notes (note, is_resolved, student_lrn, user_id, date_added, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, note.getNote());
            statement.setBoolean(2, note.isResolved());
            statement.setString(3, note.getLrn());
            statement.setInt(4, note.getUserId());
            statement.setObject(5, LocalDateTime.now());
            statement.setObject(6, LocalDateTime.now());

            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        }
    }

    public RequirementNote getRequirementNoteById(Connection conn, int noteId) throws SQLException {
        String query = "SELECT * FROM requirement_notes WHERE requirement_note_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, noteId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new RequirementNote(
                            rs.getInt("requirement_note_id"),
                            rs.getString("note"),
                            rs.getBoolean("is_resolved"),
                            rs.getString("student_lrn"),
                            rs.getInt("user_id"),
                            rs.getObject("date_added", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class)
                    );
                } else {
                    throw new SQLException("Requirement note not found.");
                }
            }
        }
    }

    public void updateNoteResolve(Connection conn, int noteId) throws SQLException {
        String query = "UPDATE requirement_notes " +
                        "SET is_resolved = ?, updated_at = ? " +
                        "WHERE requirement_note_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBoolean(1, true);
            statement.setObject(2, LocalDateTime.now());
            statement.setInt(3, noteId);

            statement.executeUpdate();
        }
    }

    public void undoResolve(Connection conn, int noteId) throws SQLException {
        String query = "UPDATE requirement_notes " +
                        "SET is_resolved = ?, updated_at = ? " +
                        "WHERE requirement_note_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBoolean(1, false);
            statement.setObject(2, LocalDateTime.now());
            statement.setInt(3, noteId);

            statement.executeUpdate();
        }
    }

    public void updateRequirementNote(Connection conn, RequirementNote note) throws SQLException {
        String query = "UPDATE requirement_notes " +
                        "SET note = ?, user_id = ?, updated_at = ? " +
                        "WHERE requirement_note_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, note.getNote());
            statement.setInt(2, note.getUserId());
            statement.setObject(3, note.getUpdatedAt());
            statement.setInt(4, note.getRequirementNoteId());

            statement.executeUpdate();
        }
    }
}
