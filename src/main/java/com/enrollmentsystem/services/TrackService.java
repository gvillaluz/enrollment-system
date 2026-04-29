package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.TrackMapper;
import com.enrollmentsystem.models.Track;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.TrackRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TrackService extends BaseService {
    private final TrackRepository _repo;

    public TrackService(TrackRepository repo, AuditRepository auditRepo) {
        super(auditRepo);
        _repo = repo;
    }

    public CompletableFuture<List<TrackDTO>> loadTracks() {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _repo.getAllTracks(conn).stream()
                        .map(TrackMapper::toDTO)
                        .toList();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load tracks.");
            }
        });
    }

    public CompletableFuture<Boolean> createTrack(TrackDTO trackDTO) {
        if (trackDTO.getTrackCode().trim().isEmpty() || trackDTO.getDescription().trim().isEmpty())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Track Code and Description must not be empty.")
            );

        validateSession();

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var track = TrackMapper.toNewModel(trackDTO);

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                Track existingTrack = _repo.findTrackByCode(conn, track.getTrackCode());

                if (existingTrack != null) {
                    if (!existingTrack.isArchived()) {
                        throw new IllegalArgumentException("Track code is already in use.");
                    } else {
                        return _repo.restoreAndUpdateTrack(conn, track) != 0;
                    }
                }

                if (_repo.existsByDescription(conn, track.getTrackDescription(), -1))
                    throw new IllegalArgumentException("Track description is already in use.");

                _repo.addTrack(conn, track);

                logActivity(
                        conn,
                        track.getTrackCode(),
                        AuditAction.ADD,
                        AuditModule.TRACK_MANAGEMENT,
                        "Added new track: " + track.getTrackCode()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to add track.");
            }
        });
    }

    public CompletableFuture<Boolean> updateTrack(TrackDTO trackDTO) {
        if (trackDTO == null) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid track")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var track = TrackMapper.toModel(trackDTO);

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                Track existingTrack = _repo.findTrackByCode(conn, track.getTrackCode());

                if (existingTrack != null && existingTrack.getTrackId() != track.getTrackId()) {
                    throw new IllegalArgumentException("Track code is already in use.");
                }

                if (_repo.existsByDescription(conn, track.getTrackDescription(), track.getTrackId())) {
                    throw new IllegalArgumentException("Track description is already in use.");
                }

                int rowsAffected = _repo.updateTrackById(conn, track);
                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        track.getTrackCode(),
                        AuditAction.UPDATE,
                        AuditModule.TRACK_MANAGEMENT,
                        "Updated track: " + track.getTrackCode()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update track.");
            }
        });
    }

    public CompletableFuture<Boolean> deleteTrack(int trackId) {
        if (trackId <= 0) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid track.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int rowsAffected = _repo.deleteTrackById(conn, trackId);
                return rowsAffected != 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to delete track.");
            }
        });
    }
}
