package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.TrackMapper;
import com.enrollmentsystem.models.Track;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.TrackRepository;

import java.util.ArrayList;
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
            var tracks = _repo.getAllTracks();

            if (tracks == null) {
                throw new IllegalArgumentException("Failed to load tracks.");
            }

            return tracks.stream()
                    .map(TrackMapper::toDTO)
                    .toList();
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
            Track existingTrack = _repo.findTrackByCode(track.getTrackCode());

            if (existingTrack != null) {
                if (!existingTrack.isArchived()) {
                    throw new IllegalArgumentException("Track code is already in use.");
                } else {
                    return _repo.restoreAndUpdateTrack(track);
                }
            }

            if (_repo.existsByDescription(track.getTrackDescription(), -1))
                throw new IllegalArgumentException("Track description is already in use.");

            Integer newTrackId = _repo.addTrack(track);

            if (newTrackId == null) {
                return false;
            }

            logActivity(newTrackId.toString(), AuditAction.ADD, AuditModule.TRACK_MANAGEMENT, "Added new track: " + newTrackId);

            return true;
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
            Track existingTrack = _repo.findTrackByCode(track.getTrackCode());

            if (existingTrack != null && existingTrack.getTrackId() != track.getTrackId()) {
                throw new IllegalArgumentException("Track code is already in use.");
            }

            if (_repo.existsByDescription(track.getTrackDescription(), track.getTrackId())) {
                throw new IllegalArgumentException("Track description is already in use.");
            }

            if (_repo.updateTrackById(track)) {
                logActivity(String.valueOf(track.getTrackId()), AuditAction.UPDATE, AuditModule.TRACK_MANAGEMENT, "Updated track: " + track.getTrackId());
                return true;
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> deleteTrack(int trackId) {
        if (trackId <= 0) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid track.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        return CompletableFuture.supplyAsync(() -> _repo.deleteTrackById(trackId));
    }
}
