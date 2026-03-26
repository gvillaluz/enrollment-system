package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Track;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
    public List<Track> getAllTracks() {
        List<Track> tracks = new ArrayList<>();
        String query = "SELECT * FROM shs_track WHERE is_archived = 0";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                var track = new Track();
                track.setTrackId(rs.getInt("shs_track_id"));
                track.setTrackCode(rs.getString("track_code"));
                track.setTrackDescription(rs.getString("track_description"));
                track.setArchived(rs.getBoolean("is_archived"));
                tracks.add(track);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public boolean addTrack(Track track) {
        String query = "INSERT INTO shs_track (track_code, track_description, is_archived) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getTrackCode());
            statement.setString(2, track.getTrackDescription());
            statement.setBoolean(3, track.isArchived());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in saving track: " + e.getMessage());
            return false;
        }
    }

    public boolean existsByDescription(String description, int trackId) {
        String query = "SELECT COUNT(*) FROM shs_track " +
                        "WHERE track_description = ? " +
                        "AND is_archived = 0 " +
                        "AND shs_track_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setInt(2, trackId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Track findTrackByCode(String trackCode) {
        String query = "SELECT * FROM shs_track " +
                        "WHERE track_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, trackCode);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    var track = new Track();
                    track.setTrackId(rs.getInt("shs_track_id"));
                    track.setTrackCode(rs.getString("track_code"));
                    track.setTrackDescription(rs.getString("track_description"));
                    track.setArchived(rs.getBoolean("is_archived"));

                    return track;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in finding track by code: " + e.getMessage());
            return null;
        }
    }

    public boolean restoreAndUpdateTrack(Track track) {
        String query = "UPDATE shs_track " +
                        "SET track_description = ?, is_archived = 0 " +
                        "WHERE track_code = ? AND is_archived = 1";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getTrackDescription());
            statement.setString(2, track.getTrackCode());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to restore and update track: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTrackById(Track track) {
        String query = "UPDATE shs_track " +
                "SET track_code = ?, track_description = ? " +
                "WHERE shs_track_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getTrackCode());
            statement.setString(2, track.getTrackDescription());
            statement.setInt(3, track.getTrackId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update track: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTrackById(int trackId) {
        String query = "UPDATE shs_track " +
                        "SET is_archived = 1 " +
                        "WHERE shs_track_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, trackId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in deleting track: " + e.getMessage());
            return false;
        }
    }
}
