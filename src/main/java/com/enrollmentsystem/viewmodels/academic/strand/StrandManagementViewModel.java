package com.enrollmentsystem.viewmodels.academic.strand;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.services.StrandService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class StrandManagementViewModel {
    private final StrandService _service = AppContext.getStrandService();
    private final ObservableList<StrandViewModel> strands = FXCollections.observableArrayList();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<StrandViewModel> getStrands() { return strands; }

    public void loadStrands() {
        isLoading.set(true);
        _service.loadStrands()
                .thenAccept(strandDTOS -> {
                    Platform.runLater(() -> {
                        strands.setAll(strandDTOS.stream()
                                .map(StrandViewModel::new)
                                .toList());

                        isLoading.set(false);
                    });
                })
                .exceptionally(ex -> {
                    System.out.println("Error to load strands: " + ex.getMessage());
                    Platform.runLater(() -> isLoading.set(false));
                    return null;
                });
    }

    public CompletableFuture<Boolean> deleteStrand(StrandViewModel strand) {
        int strandId = strand.strandIdProperty().get();

        if (strandId <= 0) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("No strand is selected.")
            );
        }

        return _service.deleteStrand(strandId)
                .thenApply(success -> {
                    if (success) {
                        Platform.runLater(this::loadStrands);
                    }
                    return success;
                });
    }
}
