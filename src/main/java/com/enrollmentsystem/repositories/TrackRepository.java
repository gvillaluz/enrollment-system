package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
    public List<Track> getAllTracks(Connection conn) throws SQLException {
        List<Track> tracks = new ArrayList<>();
        String query = "SELECT * FROM shs_track WHERE is_archived = 0";

        try (PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                var track = new Track();
                track.setTrackId(rs.getInt("shs_track_id"));
                track.setTrackCode(rs.getString("track_code"));
                track.setTrackDescription(rs.getString("track_description"));
                track.setArchived(rs.getBoolean("is_archived"));
                tracks.add(track);
            }
        }
        return tracks;
    }

    public void addTrack(Connection conn, Track track) throws SQLException {
        String query = "INSERT INTO shs_track (track_code, track_description, is_archived) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, track.getTrackCode());
            statement.setString(2, track.getTrackDescription());
            statement.setBoolean(3, track.isArchived());

            statement.executeUpdate();
        }
    }

    public boolean existsByDescription(Connection conn, String description, int trackId) throws SQLException {
        String query = "SELECT COUNT(*) FROM shs_track " +
                        "WHERE track_description = ? " +
                        "AND is_archived = 0 " +
                        "AND shs_track_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setInt(2, trackId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Track findTrackByCode(Connection conn, String trackCode) throws SQLException {
        String query = "SELECT * FROM shs_track " +
                        "WHERE track_code = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
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
        }
    }

    public int restoreAndUpdateTrack(Connection conn, Track track) throws SQLException {
        String query = "UPDATE shs_track " +
                        "SET track_description = ?, is_archived = 0 " +
                        "WHERE track_code = ? AND is_archived = 1";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getTrackDescription());
            statement.setString(2, track.getTrackCode());

            return statement.executeUpdate();
        }
    }

    public int updateTrackById(Connection conn, Track track) throws SQLException {
        String query = "UPDATE shs_track " +
                "SET track_code = ?, track_description = ? " +
                "WHERE shs_track_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getTrackCode());
            statement.setString(2, track.getTrackDescription());
            statement.setInt(3, track.getTrackId());

            return statement.executeUpdate();
        }
    }

    public int deleteTrackById(Connection conn, int trackId) throws SQLException {
        String query = "UPDATE shs_track " +
                        "SET is_archived = 1 " +
                        "WHERE shs_track_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, trackId);

            return statement.executeUpdate();
        }
    }
}
