package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.academic.section.SectionFormViewModel;
import com.enrollmentsystem.viewmodels.academic.section.SectionViewModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class SectionFormController {
    @FXML private VBox modal;
    @FXML private Label titleLabel;
    @FXML private TextField nameField, roomField;
    @FXML private ComboBox<StrandDTO> strandDropdown;

    private final SectionFormViewModel viewModel = new SectionFormViewModel();

    @FXML
    public void initialize() {
        Platform.runLater(() -> modal.requestFocus());
        bindFields();
    }

    @FXML
    public void onCancel(ActionEvent event) { closeModal(event); }

    @FXML void onSave(ActionEvent event) {
        Stage currentStage = (Stage) modal.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        if (!viewModel.hasChanges()) {
            currentStage.close();
            NotificationHelper.showToast(mainDashboard, "No changes detected.", "info");
            return;
        }

        viewModel.saveSection()
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            currentStage.close();
                            NotificationHelper.showToast(mainDashboard, "Section successfully updated.", "success");
                        } else {
                            currentStage.close();
                            NotificationHelper.showToast(mainDashboard, "Failed to update section.", "error");
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

    private void closeModal(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void bindFields() {
        nameField.textProperty().bindBidirectional(viewModel.sectionNameProperty());
        roomField.textProperty().bindBidirectional(viewModel.roomAssignmentProperty());

        strandDropdown.setConverter(new StringConverter<StrandDTO>() {
            @Override
            public String toString(StrandDTO strandDTO) {
                return (strandDTO == null) ? "" : strandDTO.getStrandCode();
            }

            @Override
            public StrandDTO fromString(String s) {
                return null;
            }
        });

        strandDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StrandDTO strandDTO, boolean empty) {
                super.updateItem(strandDTO, empty);

                if (empty || strandDTO == null) {
                    setText("<select one>");
                } else {
                    setText(strandDTO.getStrandCode());
                }
            }
        });

        strandDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                viewModel.strandIdProperty().set(newVal.getStrandId());
                viewModel.strandCodeProperty().set(newVal.getStrandCode());
            }
        });

        viewModel.getStrands().addListener((ListChangeListener<StrandDTO>) c -> {
            if (viewModel.originalSection != null) {
                for (StrandDTO strand : strandDropdown.getItems()) {
                    if (strand.getStrandId() == viewModel.strandIdProperty().get()) {
                        strandDropdown.setValue(strand);
                        break;
                    }
                }
            }
        });
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { viewModel.setOnSaveSuccess(onSaveSuccess); }

    public void setOriginalSection(SectionViewModel section) {
        viewModel.setOriginalSection(section);
        titleLabel.setText("Edit Section");
    }
}
