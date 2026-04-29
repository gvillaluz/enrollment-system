package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.academic.strand.StrandFormViewModel;
import com.enrollmentsystem.viewmodels.academic.strand.StrandManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.strand.StrandViewModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class StrandFormController {
    @FXML private VBox modal;
    @FXML private TextField codeField, descriptionField;
    @FXML private ComboBox<TrackDTO> trackCodeBox;
    @FXML private Label titleLabel;

    private final StrandFormViewModel formViewModel = new StrandFormViewModel();

    @FXML
    public void initialize() {
        Platform.runLater(() -> modal.requestFocus());
        formViewModel.loadTracks();
        setupBindings();
    }

    @FXML
    public void onCancel(ActionEvent event) { closeModal(event); }

    @FXML
    public void onSave() {
        Stage currentStage = (Stage) modal.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        String successMessage = formViewModel.originalStrand != null ? "Strand successfully updated." : "Strand successfully added.";
        String errorMessage = formViewModel.originalStrand != null ? "Failed to update strand." : "Failed to add strand.";

        formViewModel.saveStrand()
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
                        System.out.println(cause.getMessage());
                    });
                    return null;
                });
    }

    public void setupBindings() {
        codeField.textProperty().bindBidirectional(formViewModel.strandCodeProperty());
        descriptionField.textProperty().bindBidirectional(formViewModel.descriptionProperty()); 
        trackCodeBox.setPromptText("<select one>");
        trackCodeBox.setItems(formViewModel.getTracks());
        trackCodeBox.setConverter(new StringConverter<TrackDTO>() {
            @Override
            public String toString(TrackDTO trackDTO) {
                return (trackDTO == null) ? "" : trackDTO.getTrackCode();
            }

            @Override
            public TrackDTO fromString(String s) {
                return null;
            }
        });
        trackCodeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                formViewModel.trackIdProperty().set(newVal.getTrackId());
                formViewModel.trackCodeProperty().set(newVal.getTrackCode());
            }
        });

        formViewModel.getTracks().addListener((ListChangeListener<TrackDTO>) c -> {
            if (formViewModel.originalStrand != null) {
                for (TrackDTO track : trackCodeBox.getItems()) {
                    if (track.getTrackId() == formViewModel.trackIdProperty().get()) {
                        trackCodeBox.setValue(track);
                        break;
                    }
                }
            }
        });
    }

    private void closeModal(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { formViewModel.setOnSaveSuccess(onSaveSuccess); }

    public void setEditStrand(StrandViewModel strand) {
        formViewModel.setOriginalStrand(strand);

        String label = strand != null ? "Edit SHS Strand" : "Add New Strand";
        titleLabel.setText(label);
    }
}
