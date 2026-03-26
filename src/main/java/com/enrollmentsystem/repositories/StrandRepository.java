package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StrandRepository {
    public List<Strand> getAllStrands() {
        List<Strand> strands = new ArrayList<>();
        String query = "SELECT " +
                        "s.shs_strand_id, " +
                        "s.strand_code, " +
                        "s.strand_description, " +
                        "s.shs_track_id, " +
                        "t.track_code " +
                        "FROM shs_strand s " +
                        "LEFT JOIN shs_track t ON s.shs_track_id = t.shs_track_id " +
                        "WHERE s.is_archived = 0";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                var strand = new Strand();
                strand.setStrandId(rs.getInt("shs_strand_id"));
                strand.setStrandCode(rs.getString("strand_code"));
                strand.setStrandDescription(rs.getString("strand_description"));
                strand.setTrackId(rs.getInt("shs_track_id"));
                strand.setTrackCode(rs.getString("track_code"));
                strands.add(strand);
            }
            return strands;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get all strands: " + e.getMessage());
            return null;
        }
    }

    public boolean addStrand(Strand strand) {
        String query = "INSERT INTO shs_strand (strand_code, strand_description, shs_track_id, is_archived) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandCode());
            statement.setString(2, strand.getStrandDescription());
            statement.setInt(3, strand.getTrackId());
            statement.setBoolean(4, strand.isArchived());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add strand: " + e.getMessage());
            return false;
        }
    }

    public boolean existsByDescription(String description, int strandId) {
        String query = "SELECT COUNT(*) FROM shs_strand " +
                        "WHERE strand_description = ? " +
                        "AND is_archived = 0 " +
                        "AND shs_strand_id != ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setInt(2, strandId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Strand findStrandByCode(String strandCode) {
        String query = "SELECT * FROM shs_strand WHERE strand_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strandCode);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var strand = new Strand();
                    strand.setStrandId(rs.getInt("shs_strand_id"));
                    strand.setStrandCode(rs.getString("strand_code"));
                    strand.setStrandDescription(rs.getString("strand_description"));
                    strand.setTrackId(rs.getInt("shs_track_id"));
                    strand.setArchived(rs.getBoolean("is_archived"));

                    return strand;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in finding strand by code: " + e.getMessage());
            return null;
        }
    }

    public boolean restoreAndUpdateStrand(Strand strand) {
        String query = "UPDATE shs_strand " +
                        "SET strand_description = ?, is_archived = 0 " +
                        "WHERE strand_code = ? AND is_archived = 1";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandDescription());
            statement.setString(2, strand.getStrandCode());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to restore and update strand: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStrandById(Strand strand) {
        String query = "UPDATE shs_strand " +
                        "SET strand_code = ?, strand_description = ?, shs_track_id = ? " +
                        "WHERE shs_strand_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandCode());
            statement.setString(2, strand.getStrandDescription());
            statement.setInt(3, strand.getTrackId());
            statement.setInt(4, strand.getStrandId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update strand: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStrandById(int strandId) {
        String query = "UPDATE shs_strand " +
                        "SET is_archived = 1 " +
                        "WHERE shs_strand_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, strandId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete strand: " + e.getMessage());
            return false;
        }
    }
}
