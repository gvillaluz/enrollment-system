package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionRepository {
    public List<Section> getAllSections(int schoolYearId) {
        List<Section> sections = new ArrayList<>();
        String query = "SELECT * FROM sections WHERE school_year_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, schoolYearId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var section = new Section();
                    section.setSectionId(rs.getInt("section_id"));
                    section.setName(rs.getString("name"));
                    section.setMaxCap(rs.getInt("max_cap"));
                    section.setSchoolYearId(rs.getInt("school_year_id"));
                    section.setStrandId(rs.getInt("shs_strand_id"));
                    section.setRoomAssignment(rs.getString("room_assignment"));
                    sections.add(section);
                }
            }

            return sections;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to load sections: " + e.getMessage());
            throw new IllegalArgumentException("Failed to load sections: " + e.getMessage());
        }
    }
}
