package com.enrollmentsystem.controllers.login;

import com.enrollmentsystem.App;
import com.enrollmentsystem.viewmodels.login.ChangeDefaultViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class ChangeDefaultController {
    @FXML
    private StackPane root, passwordContainer;
    @FXML private TextField newTextField, confirmTextField;
    @FXML private PasswordField newPasswordField, confirmNewPassword;
    @FXML private Button saveBtn, newEyeBtn, reEnterEyeBtn;
    @FXML private Label errorLabel;

    private final ChangeDefaultViewModel viewModel = new ChangeDefaultViewModel();
    private final FontIcon eyeIcon = new FontIcon("fas-eye");
    private final ProgressIndicator indicator = new ProgressIndicator();

    @FXML
    public void initialize() {
        Platform.runLater(() -> root.requestFocus());
        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());

        setupPasswordField();
        setupBindings();

        newEyeBtn.setGraphic(new FontIcon("fas-eye"));
        reEnterEyeBtn.setGraphic(new FontIcon("fas-eye"));

        newEyeBtn.setOnAction(e -> toggleVisibility(newPasswordField, newTextField, newEyeBtn));
        reEnterEyeBtn.setOnAction(e -> toggleVisibility(confirmNewPassword, confirmTextField, reEnterEyeBtn));

        saveBtn.setDefaultButton(true);
    }

    @FXML
    public void handleUpdatePass() {
        viewModel.updatePassword()
                .thenAccept(success -> {
                    if (success) {
                        goToDashboard();
                    }
                });
    }

    private void goToDashboard() {
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/core/Main.fxml"));
            Parent dashboardRoot = dashboardLoader.load();

            Scene currentScene = root.getScene();
            Stage stage = (Stage) currentScene.getWindow();

            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/core/main.css")).toExternalForm()
            );

            currentScene.setRoot(dashboardRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBindings() {
        newPasswordField.textProperty().bindBidirectional(viewModel.newPasswordProperty());
        newTextField.textProperty().bindBidirectional(newPasswordField.textProperty());

        confirmNewPassword.textProperty().bindBidirectional(viewModel.confirmNewPassword());
        confirmTextField.textProperty().bindBidirectional(confirmNewPassword.textProperty());
        errorLabel.textProperty().bind(viewModel.errorLabelProperty());

        newPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isValid = newVal.length() >= 8 && newVal.matches(".*[A-Z].*") && newVal.matches(".*[0-9].*");

            updateFieldStyle(newPasswordField, isValid);
            updateFieldStyle(newTextField, isValid);
        });

        confirmNewPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isMatch = !newVal.isEmpty() && newVal.equals(newPasswordField.getText());

            updateFieldStyle(confirmNewPassword, isMatch);
            updateFieldStyle(confirmTextField, isMatch);
        });

        viewModel.loadingProperty().addListener((obs, oldVal, isLoading) -> {
            if (isLoading){
                saveBtn.setDisable(true);
                saveBtn.setGraphic(indicator);
                saveBtn.setText(null);
            } else {
                saveBtn.setDisable(false);
                saveBtn.setGraphic(indicator);
                saveBtn.setText("Update Password");
            }
        });
    }

    private void setupPasswordField() {
        newPasswordField.setVisible(true);
        newPasswordField.setManaged(true);
        newTextField.setVisible(false);
        newTextField.setManaged(false);

        confirmNewPassword.setVisible(true);
        confirmNewPassword.setManaged(true);
        confirmTextField.setVisible(false);
        confirmTextField.setManaged(false);

        eyeIcon.setIconSize(16);
        eyeIcon.getStyleClass().add("password-eye-icon");
        newEyeBtn.setGraphic(eyeIcon);
        reEnterEyeBtn.setGraphic(eyeIcon);

        StackPane.setAlignment(newEyeBtn, javafx.geometry.Pos.CENTER_RIGHT);
        StackPane.setMargin(newEyeBtn, new javafx.geometry.Insets(0, 12, 0, 0));

        StackPane.setAlignment(reEnterEyeBtn, javafx.geometry.Pos.CENTER_RIGHT);
        StackPane.setMargin(reEnterEyeBtn, new javafx.geometry.Insets(0, 12, 0, 0));

        newEyeBtn.toFront();
        reEnterEyeBtn.toFront();
    }

    private void toggleVisibility(PasswordField pf, TextField tf, Button btn) {
        boolean isMasked = pf.isVisible();
        FontIcon icon = (FontIcon) btn.getGraphic();

        if (isMasked) {
            pf.setVisible(false);
            pf.setManaged(false);
            tf.setVisible(true);
            tf.setManaged(true);
            icon.setIconLiteral("fas-eye-slash");
            tf.requestFocus();
            tf.selectEnd();
        } else {
            tf.setVisible(false);
            tf.setManaged(false);
            pf.setVisible(true);
            pf.setManaged(true);
            icon.setIconLiteral("fas-eye");
            pf.requestFocus();
            pf.selectEnd();
        }
    }

    private void updateFieldStyle(Control field, boolean isValid) {
        if (isValid) {
            field.getStyleClass().remove("field-error");
            if (!field.getStyleClass().contains("field-success")) {
                field.getStyleClass().add("field-success");
            }
        } else {
            field.getStyleClass().remove("field-success");
            if (!field.getStyleClass().contains("field-error")) {
                field.getStyleClass().add("field-error");
            }
        }
    }
}
