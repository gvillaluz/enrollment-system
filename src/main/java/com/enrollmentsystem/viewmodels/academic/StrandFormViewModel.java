package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.models.Track;
import com.enrollmentsystem.services.StrandService;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class StrandFormViewModel {
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackCode = new SimpleStringProperty();
    private final StringProperty strandCode = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public StrandViewModel originalStrand;
    private Runnable onSaveSuccess;
    private final ObservableList<TrackDTO> tracks = FXCollections.observableArrayList();
    private final StrandService _service = AppContext.getStrandService();

    public StringProperty trackCodeProperty() { return trackCode; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public StringProperty descriptionProperty() { return description; }
    public IntegerProperty trackIdProperty() { return trackId; }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { this.onSaveSuccess = onSaveSuccess; }

    public ObservableList<TrackDTO> getTracks() { return tracks; }

    public void loadTracks() {
        tracks.clear();
        AppContext.getTrackService().loadTracks()
                .thenAccept(trackDTOS -> {
                    Platform.runLater(() -> {
                        tracks.addAll(trackDTOS);
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.out.println("Error in loading tracks: " + ex.getMessage());
                    return null;
                });
    }

    public void setOriginalStrand(StrandViewModel originalStrand) {
        this.originalStrand = originalStrand;

        if (originalStrand != null) {
            strandId.set(originalStrand.strandIdProperty().get());
            trackId.set(originalStrand.trackIdProperty().get());
            trackCode.set(originalStrand.trackCodeProperty().get());
            strandCode.set(originalStrand.strandCodeProperty().get());
            description.set(originalStrand.descriptionProperty().get());
        } else {
            strandId.set(0);
            trackId.set(0);
            trackCode.set("");
            strandCode.set("");
            description.set("");
        }
    }

    public CompletableFuture<Boolean> saveStrand() {
        String code = strandCode.get();
        String desc = description.get();
        int trackId = this.trackId.get();

        if (originalStrand == null) {
            if (ValidationHelper.isNullOrEmpty(code) || ValidationHelper.isNullOrEmpty(desc) || trackId <= 0)
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("All fields must not be empty.")
                );

            var strandDTO = new StrandDTO();
            strandDTO.setStrandCode(code);
            strandDTO.setDescription(desc);
            strandDTO.setTrackId(trackId);

            return _service.createStrand(strandDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        } else {
            if (ValidationHelper.isNullOrEmpty(code) || ValidationHelper.isNullOrEmpty(desc) || trackId <= 0)
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("All fields must not be empty.")
                );

            if (originalStrand.strandCodeProperty().get().equals(code) && originalStrand.descriptionProperty().get().equals(desc) && originalStrand.trackIdProperty().get() == trackId) {
                return CompletableFuture.completedFuture(true);
            }

            var strandDTO = new StrandDTO();
            strandDTO.setStrandId(strandId.get());
            strandDTO.setStrandCode(code);
            strandDTO.setDescription(desc);
            strandDTO.setTrackId(trackId);

            return _service.updateStrand(strandDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        }
    }
}
