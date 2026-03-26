package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.models.Track;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.TrackRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TrackService {
    private final TrackRepository _repo;

    public TrackService(TrackRepository repo) {
        _repo = repo;
    }

    public CompletableFuture<List<TrackDTO>> loadTracks() {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            List<TrackDTO> trackDTOs = new ArrayList<>();
            var tracks = _repo.getAllTracks();

            if (tracks == null) {
                throw new IllegalArgumentException("Failed to load tracks.");
            }

            for (Track track : tracks) {
                trackDTOs.add(new TrackDTO(
                        track.getTrackId(),
                        track.getTrackCode(),
                        track.getTrackDescription(),
                        track.isArchived()
                ));
            }

            return trackDTOs;
        });
    }

    public CompletableFuture<Boolean> createTrack(TrackDTO trackDTO) {
        if (trackDTO.getTrackCode().trim().isEmpty() || trackDTO.getDescription().trim().isEmpty())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Track Code and Description must not be empty.")
            );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var track = new Track();
        track.setTrackId(trackDTO.getTrackId());
        track.setTrackCode(trackDTO.getTrackCode());
        track.setTrackDescription(trackDTO.getDescription());
        track.setArchived(false);

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

            return _repo.addTrack(track);
        });
    }

    public CompletableFuture<Boolean> updateTrack(TrackDTO trackDTO) {
        if (trackDTO == null) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid track")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var track = new Track();
        track.setTrackId(trackDTO.getTrackId());
        track.setTrackCode(trackDTO.getTrackCode());
        track.setTrackDescription(trackDTO.getDescription());

        return CompletableFuture.supplyAsync(() -> {
            Track existingTrack = _repo.findTrackByCode(track.getTrackCode());

            if (existingTrack != null && existingTrack.getTrackId() != track.getTrackId()) {
                throw new IllegalArgumentException("Track code is already in use.");
            }

            if (_repo.existsByDescription(track.getTrackDescription(), track.getTrackId())) {
                throw new IllegalArgumentException("Track description is already in use.");
            }

            return _repo.updateTrackById(track);
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
