package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class TrackManagementViewModel {
    private final TrackService _service = AppContext.getTrackService();
    private final ObservableList<TrackViewModel> tracks = FXCollections.observableArrayList();

    public ObservableList<TrackViewModel> getTracks() { return tracks; }

    public void loadTracks() {
        tracks.clear();
        _service.loadTracks()
                .thenAccept(trackDTOS -> {
                    Platform.runLater(() -> {
                        for (TrackDTO dto : trackDTOS) {
                            tracks.add(new TrackViewModel(dto));
                        }
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.out.println("Error in loading tracks: " + ex.getMessage());
                    return null;
                });
    }



    public CompletableFuture<Boolean> deleteTrack(TrackViewModel track) {
        int trackId = track.trackIdProperty().get();

        if (trackId <= 0) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("No track is selected")
            );
        }

        return _service.deleteTrack(trackId)
                .thenApply(success -> {
                    if (success) {
                        Platform.runLater(this::loadTracks);
                    }
                    return success;
                });
    }
}
