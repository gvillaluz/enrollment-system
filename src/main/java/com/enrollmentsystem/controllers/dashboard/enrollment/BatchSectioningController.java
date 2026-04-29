package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.App;
import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.services.BatchSectioningService;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.StringFormatter;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.enrollment.batchsectioning.BatchSectioningViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BatchSectioningController {
    @FXML private TextField sizeField;
    @FXML private VBox content;
    @FXML private Label feedbackLabel;
    @FXML private HBox feedbackContainer;
    @FXML private Button runBtn;

    private final BatchSectioningViewModel viewModel = new BatchSectioningViewModel();

    private final ProgressIndicator loadingSpinner = new ProgressIndicator();

    @FXML
    public void initialize() {
        bindField();

        loadingSpinner.setMaxSize(25, 25);

        feedbackContainer.getChildren().add(loadingSpinner);

        feedbackLabel.textProperty().bind(viewModel.statusMessageProperty());
        feedbackContainer.visibleProperty().bind(viewModel.showStatusProperty());
        feedbackContainer.managedProperty().bind(viewModel.showStatusProperty());

        loadingSpinner.visibleProperty().bind(viewModel.isProcessingProperty());
        loadingSpinner.managedProperty().bind(viewModel.isProcessingProperty());

        viewModel.checkSections();

        content.setOnMouseClicked(event -> Platform.runLater(() -> content.requestFocus()));
    }

    @FXML
    public void onRun(ActionEvent event) {
        String currentSY = viewModel.schoolYearProperty().get();

        if (currentSY == null || currentSY.trim().isEmpty()) {
            runBatchSectioning();
        } else {
            openReRunConfirmationModal();
        }
    }

    private void bindField() {
        TextFormatter<Integer> sizeFormatter = StringFormatter.formatStringToInteger(0);
        sizeField.setTextFormatter(sizeFormatter);
        sizeFormatter.valueProperty().bindBidirectional(viewModel.maxCapProperty());

        runBtn.disableProperty().bind(viewModel.isProcessingProperty());
    }

    private void changeLabelStyles(boolean isSuccess) {
        feedbackContainer.getStyleClass().removeAll("success", "failed");

        if (isSuccess) {
            feedbackContainer.getStyleClass().add("success");
        } else {
            feedbackContainer.getStyleClass().add("failed");
        }
    }

    public void runBatchSectioning() {
        feedbackContainer.getStyleClass().removeAll("success", "failed");

        Window currentStage = content.getScene().getWindow();

        viewModel.activateSectioning()
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            NotificationHelper.showToast(
                                    currentStage,
                                    "Batch sectioning completed successfully!",
                                    "success"
                            );
                        } else {
                            NotificationHelper.showToast(
                                    currentStage,
                                    "Process failed. No changes were saved.",
                                    "error"
                            );
                        }

                        changeLabelStyles(success);
                    });
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                    Platform.runLater(() -> {
                        NotificationHelper.showToast(currentStage, cause.getMessage(), "error");
                    });

                    changeLabelStyles(false);
                    return null;
                });
    }

    public void openReRunConfirmationModal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/enrollment/ReRunConfirmationModal.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) content.getScene().getWindow();

            ReRunBatchController controller = loader.getController();
            controller.setOnConfirmAction(this::runBatchSectioning);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
