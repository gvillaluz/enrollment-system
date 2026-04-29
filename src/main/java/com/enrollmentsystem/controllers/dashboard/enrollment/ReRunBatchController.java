package com.enrollmentsystem.controllers.dashboard.enrollment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ReRunBatchController {
    @FXML private HBox iconContainer;

    private Runnable onConfirmAction;

    @FXML
    public void initialize() {

    }

    @FXML
    public void onConfirm(ActionEvent event) {
        onConfirmAction.run();
        close(event);
    }

    @FXML
    public void onCancel(ActionEvent event) {
        close(event);
    }

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;

        setupIcon();
    }

    private void setupIcon() {
        FontIcon warningIcon = new FontIcon("fas-exclamation-triangle");
        warningIcon.getStyleClass().add("warning-icon");
        warningIcon.setIconSize(50);
        iconContainer.getChildren().add(warningIcon);
    }

    private void close(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
