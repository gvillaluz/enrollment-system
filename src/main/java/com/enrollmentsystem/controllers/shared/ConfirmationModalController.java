package com.enrollmentsystem.controllers.shared;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ConfirmationModalController {

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

    public void setup(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }

    private void close(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
