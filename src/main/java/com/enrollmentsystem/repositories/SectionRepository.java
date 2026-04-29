package com.enrollmentsystem.repositories;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionRepository {
    public List<SectionDTO> getAllSections(Connection conn, int schoolYearId) throws SQLException {
        List<SectionDTO> sections = new ArrayList<>();
        String query = "SELECT sec.section_id, sec.name, sec.school_year_id, sec.shs_strand_id, sec.room_assignment, " +
                        "str.strand_code, " +
                        "CONCAT('SY ', sy.start_year, '-', sy.end_year) AS school_year " +
                        "FROM sections sec " +
                        "LEFT JOIN shs_strand str ON sec.shs_strand_id = str.shs_strand_id " +
                        "LEFT JOIN school_year sy ON sec.school_year_id = sy.school_year_id " +
                        "WHERE sec.school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    sections.add(new SectionDTO(
                            rs.getInt("section_id"),
                            rs.getString("name"),
                            rs.getInt("school_year_id"),
                            rs.getString("school_year"),
                            rs.getInt("shs_strand_id"),
                            rs.getString("strand_code"),
                            rs.getString("room_assignment")
                    ));
                }
            }

            return sections;
        }
    }

    public Section getSectionById(Connection conn, int sectionId) throws SQLException {
        String query = "SELECT * FROM sections WHERE section_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, sectionId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Section(
                            rs.getInt("section_id"),
                            rs.getString("name"),
                            rs.getInt("school_year_id"),
                            rs.getInt("shs_strand_id"),
                            rs.getString("room_assignment")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void updateSectionById(Connection conn, Section section) throws SQLException {
        String query = "UPDATE sections " +
                        "SET name = ?, shs_strand_id = ?, room_assignment = ? " +
                        "WHERE section_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, section.getName());
            statement.setInt(2, section.getStrandId());
            statement.setString(3, section.getRoomAssignment());
            statement.setInt(4, section.getSectionId());

            statement.executeUpdate();
        }
    }

    public boolean checkSectionsExists(Connection conn, int schoolYearId) throws SQLException {
        String query = "SELECT COUNT(*) FROM sections WHERE school_year_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
