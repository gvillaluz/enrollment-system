package com.enrollmentsystem.viewmodels.enrollment.batchsectioning;

import com.enrollmentsystem.App;
import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.controllers.dashboard.academic.SYActivationModalController;
import com.enrollmentsystem.controllers.dashboard.enrollment.ReRunBatchController;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.services.BatchSectioningService;
import com.enrollmentsystem.utils.DateFormatter;
import com.enrollmentsystem.utils.ViewNavigator;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BatchSectioningViewModel {
    private final ObjectProperty<Integer> maxCap = new SimpleObjectProperty<>();
    private final BooleanProperty isLoading = new SimpleBooleanProperty();
    private final StringProperty schoolYear = new SimpleStringProperty();
    private final BooleanProperty isProcessing = new SimpleBooleanProperty(false);
    private final StringProperty statusMessage = new SimpleStringProperty("");
    private final BooleanProperty showStatus = new SimpleBooleanProperty(false  );

    private final BatchSectioningService _service = AppContext.getBatchSectioningService();

    public ObjectProperty<Integer> maxCapProperty() { return maxCap; }
    public BooleanProperty loadingProperty() { return isLoading; }
    public BooleanProperty isProcessingProperty() { return isProcessing; }
    public StringProperty statusMessageProperty() { return statusMessage; }
    public BooleanProperty showStatusProperty() { return showStatus; }
    public StringProperty schoolYearProperty() { return schoolYear; }

    public void checkSections() {
        _service.getBatchSectionAudit()
                .thenAccept(dto -> {
                    Platform.runLater(() -> {
                        if (dto != null) {
                            schoolYear.set(dto.getSchoolYear());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a");
                            statusMessage.set("Finalized by " + dto.getProcessedBy() + " for " + dto.getSchoolYear() +  " on " + dto.getProcessedAt().format(formatter));
                            showStatus.set(true);
                        } else {
                            showStatus.set(false);
                        }
                    });
                });
    }

    public CompletableFuture<Boolean> activateSectioning() {
        System.out.println(maxCap.get());

        isProcessing.set(true);
        statusMessage.set("Processing . . .");
        showStatus.set(true);

        Integer currentCap = maxCap.get();

        if (currentCap == null) {
            statusMessage.set("Please enter a capacity value.");
            isProcessing.set(false);
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Target capacity cannot be empty.")
            );
        }

        if (currentCap <= 25 || currentCap > 40) {
            statusMessage.set("Invalid Target Capacity (25-40).");
            isProcessing.set(false);
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Capacity must be between 25 and 40.")
            );
        }

        return _service.runBatchSectioning(maxCap.get())
                .thenApply(schoolYear -> {
                    Platform.runLater(() -> {
                        isProcessing.set(false);
                        if (schoolYear != null && !schoolYear.isEmpty()) {

                            this.schoolYear.set(schoolYear);

                            String admin = UserSession.getInstance().getUser().getFirstName() + " " + UserSession.getInstance().getUser().getLastName();
                            String now = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a"));

                            statusMessage.set("Finalized by " + admin + " for " + schoolYear +  " on " + now);

                            maxCap.set(null);
                        } else {
                            showStatus.set(false);
                        }
                    });
                    return true;
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    Platform.runLater(() -> {
                        isProcessing.set(false);
                        statusMessage.set(cause.getMessage());
                    });
                    return false;
                });
    }
}
