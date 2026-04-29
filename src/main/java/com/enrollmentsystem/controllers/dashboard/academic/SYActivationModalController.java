package com.enrollmentsystem.controllers.dashboard.academic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class SYActivationModalController {
    @FXML private Label titleLabel, messageLabel;
    @FXML private HBox iconContainer;
    @FXML private Button confirmBtn;

    private Runnable onConfirmAction;

    @FXML
    public void initialize() {}

    @FXML
    public void onConfirm(ActionEvent event) {
        onConfirmAction.run();
        close(event);
    }

    @FXML
    public void onCancel(ActionEvent event) {
        close(event);
    }

    public void setOnConfirmAction(Runnable onConfirmAction) { this.onConfirmAction = onConfirmAction; }

    public void setup(boolean isCreating) {
        if (isCreating) {
            titleLabel.setText("Create New School Year");
            messageLabel.setText("Creating a new school year will automatically close the current active school year. Do you wish to proceed?");
            confirmBtn.setText("Create");
        } else {
            titleLabel.setText("Activate School Year");
            messageLabel.setText("Are you sure you want to set the selected academic year as the active one? This action will deactivate the currently active school year.");
            confirmBtn.setText("Confirm Activation");
        }

        setupIcon();
    }

    private void close(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void setupIcon() {
        FontIcon warningIcon = new FontIcon("fas-exclamation-triangle");
        warningIcon.getStyleClass().add("warning-icon");
        warningIcon.setIconSize(50);
        iconContainer.getChildren().add(warningIcon);
    }
}
