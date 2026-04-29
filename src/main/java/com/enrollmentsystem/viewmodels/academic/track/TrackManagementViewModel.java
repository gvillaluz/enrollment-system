package com.enrollmentsystem.viewmodels.academic.track;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.services.TrackService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class TrackManagementViewModel {
    private final TrackService _service = AppContext.getTrackService();
    private final ObservableList<TrackViewModel> tracks = FXCollections.observableArrayList();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<TrackViewModel> getTracks() { return tracks; }

    public void loadTracks() {
        isLoading.set(true);
        _service.loadTracks()
                .thenAccept(trackDTOS -> {
                    Platform.runLater(() -> {
                        tracks.setAll(trackDTOS.stream()
                                .map(TrackViewModel::new)
                                .toList());

                        isLoading.set(false);
                    });
                })
                .exceptionally(ex -> {
                    System.out.println("Error in loading tracks: " + ex.getMessage());
                    Platform.runLater(() -> isLoading.set(false));
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
