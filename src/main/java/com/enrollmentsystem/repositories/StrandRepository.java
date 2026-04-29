package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Strand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StrandRepository {
    public List<Strand> getAllStrands(Connection conn) throws SQLException {
        List<Strand> strands = new ArrayList<>();
        String query = "SELECT " +
                        "s.shs_strand_id, " +
                        "s.strand_code, " +
                        "s.strand_description, " +
                        "s.shs_track_id, " +
                        "t.track_code " +
                        "FROM shs_strand s " +
                        "LEFT JOIN shs_track t ON s.shs_track_id = t.shs_track_id " +
                        "WHERE s.is_archived = 0 AND t.is_archived = 0";

        try (PreparedStatement statement = conn.prepareStatement(query);
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
        }
    }

    public void addStrand(Connection conn, Strand strand) throws SQLException {
        String query = "INSERT INTO shs_strand (strand_code, strand_description, shs_track_id, is_archived) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandCode());
            statement.setString(2, strand.getStrandDescription());
            statement.setInt(3, strand.getTrackId());
            statement.setBoolean(4, strand.isArchived());

            statement.executeUpdate();
        }
    }

    public boolean existsByDescription(Connection conn, String description, int strandId) throws SQLException {
        String query = "SELECT COUNT(*) FROM shs_strand " +
                        "WHERE strand_description = ? " +
                        "AND is_archived = 0 " +
                        "AND shs_strand_id != ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setInt(2, strandId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Strand findStrandByCode(Connection conn, String strandCode) throws SQLException {
        String query = "SELECT * FROM shs_strand WHERE strand_code = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
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
        }
    }

    public int restoreAndUpdateStrand(Connection conn, Strand strand) throws SQLException {
        String query = "UPDATE shs_strand " +
                        "SET strand_description = ?, is_archived = 0 " +
                        "WHERE strand_code = ? AND is_archived = 1";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandDescription());
            statement.setString(2, strand.getStrandCode());

            return statement.executeUpdate();
        }
    }

    public int updateStrandById(Connection conn, Strand strand) throws SQLException {
        String query = "UPDATE shs_strand " +
                        "SET strand_code = ?, strand_description = ?, shs_track_id = ? " +
                        "WHERE shs_strand_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, strand.getStrandCode());
            statement.setString(2, strand.getStrandDescription());
            statement.setInt(3, strand.getTrackId());
            statement.setInt(4, strand.getStrandId());

            return statement.executeUpdate();
        }
    }

    public int deleteStrandById(Connection conn, int strandId) throws SQLException {
        String query = "UPDATE shs_strand " +
                        "SET is_archived = 1 " +
                        "WHERE shs_strand_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, strandId);

            return statement.executeUpdate();
        }
    }
}
