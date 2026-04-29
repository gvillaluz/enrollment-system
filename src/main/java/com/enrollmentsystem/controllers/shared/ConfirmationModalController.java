package com.enrollmentsystem.controllers.shared;

import com.enrollmentsystem.utils.filters.ModalConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationModalController {
    @FXML private Label titleLabel, messageLabel;
    @FXML private Button confirmBtn;

    private Runnable onConfirmAction;

    @FXML
    public void initialize() {}

    @FXML
    public void onConfirmAction(ActionEvent event) {
        if (onConfirmAction != null) onConfirmAction.run();
        close(event);
    }

    @FXML
    public void onCancelClick(ActionEvent event) {
        close(event);
    }

    public void setup(ModalConfig config) {
        this.onConfirmAction = config.getOnConfirm();
        titleLabel.setText(config.getTitle());
        messageLabel.setText(config.getMessage());

        if (config.getTitle().equals("Delete")) {
            confirmBtn.getStyleClass().add("delete");
            confirmBtn.setText("Delete");
        } else if (config.getTitle().equals("⚠️ Warning: Existing Assignments Detected")) {
            confirmBtn.getStyleClass().add("warning");
            confirmBtn.setText("Reset");
        } else {
            confirmBtn.getStyleClass().add("archive");
            confirmBtn.setText("Continue");
        }
    }

    private void close(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
