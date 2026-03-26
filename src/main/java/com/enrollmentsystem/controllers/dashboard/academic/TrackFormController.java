package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.academic.TrackFormViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TrackFormController {
    @FXML private VBox modal;
    @FXML private TextField codeField, descriptionField;
    @FXML private Label titleLabel;

    TrackFormViewModel formViewModel = new TrackFormViewModel();
    TrackManagementViewModel viewModel;

    @FXML
    public void initialize() {
        Platform.runLater(() -> modal.requestFocus());
        setupTextFields();
    }

    @FXML
    public void onCancel(ActionEvent event) {
        closeModal(event);
    }

    @FXML
    public void onSave(ActionEvent event) {
        Stage currentStage = (Stage) modal.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        String successMessage = formViewModel.originalTrack != null ? "Track successfully updated." : "Track successfully added.";
        String errorMessage = formViewModel.originalTrack != null ? "Failed to update track." : "Failed to add track.";

        formViewModel.saveTrack()
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            currentStage.close();
                            NotificationHelper.showToast(mainDashboard, successMessage, "success");
                        } else {
                            NotificationHelper.showToast(mainDashboard, errorMessage, "error");
                        }
                    });
                })
                .exceptionally(ex -> {
                   Platform.runLater(() -> {
                       Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                       NotificationHelper.showToast(mainDashboard, cause.getMessage(), "error");
                       ex.printStackTrace();
                   });
                    return null;
                });
    }

    private void setupTextFields() {
        codeField.textProperty().bindBidirectional(formViewModel.trackCodeProperty());
        descriptionField.textProperty().bindBidirectional(formViewModel.descriptionProperty());
    }

    private void closeModal(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { formViewModel.setOnSaveSuccess(onSaveSuccess); }

    public void setEditTrack(TrackViewModel track) {
        formViewModel.setOriginalTrack(track);

        String label = track != null ? "Edit SHS Track" : "Add New Track";
        titleLabel.setText(label);
    }

    public void setViewModel(TrackManagementViewModel viewModel) { this.viewModel = viewModel; }
}
