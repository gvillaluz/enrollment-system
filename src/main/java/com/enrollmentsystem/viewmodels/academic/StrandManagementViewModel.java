package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.services.StrandService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class StrandManagementViewModel {
    private final StrandService _service = AppContext.getStrandService();
    private final ObservableList<StrandViewModel> strands = FXCollections.observableArrayList();

    public ObservableList<StrandViewModel> getStrands() { return strands; }

    public void loadStrands() {
        strands.clear();
        _service.loadStrands()
                .thenAccept(strandDTOS -> {
                    Platform.runLater(() -> {
                        for (StrandDTO dto : strandDTOS) {
                            strands.add(new StrandViewModel(dto));
                        }
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.out.println("Error to load strands: " + ex.getMessage());
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
