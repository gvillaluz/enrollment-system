package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.CompletableFuture;

public class TrackFormViewModel {
    private final StringProperty trackCode = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public TrackViewModel originalTrack;
    private Runnable onSaveSuccess;
    private final TrackService _service = AppContext.getTrackService();

    public StringProperty trackCodeProperty() { return trackCode; }
    public StringProperty descriptionProperty() { return description; }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { this.onSaveSuccess = onSaveSuccess; }

    public void setOriginalTrack(TrackViewModel originalTrack) {
        this.originalTrack = originalTrack;

        if (originalTrack != null) {
            trackCode.set(originalTrack.trackCodeProperty().get());
            description.set(originalTrack.descriptionProperty().get());
        } else {
            trackCode.set("");
            description.set("");
        }
    }

    public CompletableFuture<Boolean> saveTrack() {
        String code = trackCode.get();
        String desc = description.get();

        if (originalTrack == null) {
            if (ValidationHelper.isNullOrEmpty(code) || ValidationHelper.isNullOrEmpty(desc))
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Track Code and Description must not be empty.")
                );

            var trackDTO = new TrackDTO();
            trackDTO.setTrackCode(code);
            trackDTO.setDescription(desc);

            return _service.createTrack(trackDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        } else {
            if (ValidationHelper.isNullOrEmpty(code) || ValidationHelper.isNullOrEmpty(desc))
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Track Code and Description must not be empty.")
                );

            if (originalTrack.trackCodeProperty().get().equals(code) && originalTrack.descriptionProperty().get().equals(desc)) {
                return CompletableFuture.completedFuture(true);
            }

            var trackDTO = new TrackDTO();
            trackDTO.setTrackId(originalTrack.trackIdProperty().get());
            trackDTO.setTrackCode(code);
            trackDTO.setDescription(desc);

            return _service.updateTrack(trackDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        }
    }
}
