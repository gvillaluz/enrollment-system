package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.StringFormatter;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.schoolyear.SchoolYearFormViewModel;
import com.enrollmentsystem.viewmodels.academic.schoolyear.SchoolYearViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class SYFormController {
    @FXML private VBox modal;
    @FXML private Label titleLabel;

    @FXML private TextField startField, endField;

    private final SchoolYearFormViewModel viewModel = new SchoolYearFormViewModel();

    @FXML
    public void initialize() {
        Platform.runLater(() -> modal.requestFocus());

        TextFormatter<Integer> startFormatter = StringFormatter.formatStringToInteger(null);
        TextFormatter<Integer> endFormatter = StringFormatter.formatStringToInteger(null);

        startField.setTextFormatter(startFormatter);
        endField.setTextFormatter(endFormatter);

        startField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.isEmpty()) {
                try {
                    startFormatter.setValue(Integer.parseInt(newText));
                } catch (NumberFormatException ignored) {}
            }
        });

        startFormatter.valueProperty().bindBidirectional(viewModel.startYearProperty());
        endFormatter.valueProperty().bindBidirectional(viewModel.endYearProperty());
    }

    @FXML
    public void onCancel(ActionEvent event) {
        closeModal(event);
    }

    @FXML
    public void onSave(ActionEvent event) {
        if (viewModel.originalSy == null) {
            loadConfirmModal();
        } else {
            handleSave();
        }
    }

    private void loadConfirmModal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/SchoolYearConfirmModal.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) modal.getScene().getWindow();

            SYActivationModalController controller = loader.getController();
            controller.setOnConfirmAction(this::handleSave);
            controller.setup(true);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleSave() {
        Stage currentStage = (Stage) modal.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        String successMessage = viewModel.originalSy != null ? "School year successfully updated." : "School year successfully added.";
        String errorMessage = viewModel.originalSy != null ? "Failed to update school year." : "Failed to add school year.";

        viewModel.saveSchoolYear()
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
                    });
                    return null;
                });
    }

    private void closeModal(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { viewModel.setOnSaveSuccess(onSaveSuccess); }
    public void setOriginalSy(SchoolYearViewModel sy) {
        viewModel.setOriginalSy(sy);

        String label = sy != null ? "Edit School Year" : "Create New";
        titleLabel.setText(label);
    }
}
