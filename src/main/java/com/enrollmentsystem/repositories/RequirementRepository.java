package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.models.StudentRequirement;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequirementRepository {
    public Integer getRequirementRowCount(int schoolYearId, String searchValue) {
        String query = "SELECT COUNT(DISTINCT e.student_lrn) FROM enrollments e " +
                "LEFT JOIN student s ON e.student_lrn = s.student_lrn " +
                "WHERE e.school_year_id = ? ";

        if (searchValue != null) {
            query += "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE (?))";
            searchValue = "%" + searchValue + "%";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            if (searchValue != null) {
                statement.setString(2, searchValue);
                statement.setString(3, searchValue);
                statement.setString(4, searchValue);
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get requirement row count" + e.getMessage());
            return null;
        }
    }

    public List<RequirementSummaryDTO> getAllChecklist(int schoolYearId, String searchValue, int offset) {
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
                "    MAX(CASE WHEN sr.requirement_ref_id = 7 THEN sr.is_submitted ELSE 0 END) AS als_coc " +
                "FROM student s " +
                "LEFT JOIN student_requirements sr ON s.student_lrn = sr.student_lrn " +
                "LEFT JOIN enrollments e ON s.student_lrn = e.student_lrn " +
                "WHERE e.enrollment_status = 'Enrolled' AND e.school_year_id = ? " +
                (searchValue != null ? "AND (s.student_lrn LIKE ? OR LOWER(s.last_name) LIKE LOWER(?) OR LOWER(s.first_name) LIKE LOWER(?)) " : "") +
                "GROUP BY s.student_lrn, s.last_name, s.first_name, s.middle_name " +
                "LIMIT 12 OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

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

                    checklist.add(requirementDto);
                }
            }

            return checklist;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load checklist");
        }
    }

    public List<StudentRequirement> getStudentRequirementsByLRN(String lrn) {
        List<StudentRequirement> requirements = new ArrayList<>();
        String query = "SELECT * FROM student_requirements WHERE student_lrn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Failed to load requirements for editing: " + e.getMessage());
            throw new IllegalArgumentException("Failed to load requirements");
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

    public boolean updateRequirement(String lrn, int refId, boolean isSubmitted) {
        String query = "UPDATE student_requirements " +
                        "SET is_submitted = ? " +
                        "WHERE student_lrn = ? AND requirement_ref_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBoolean(1, isSubmitted);
            statement.setString(2, lrn);
            statement.setInt(3, refId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sync requirement: " + e.getMessage());
            return false;
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
            e.printStackTrace();
            System.out.println("Failed to load requirements by name: " + e.getMessage());
            return null;
        }
    }
}
