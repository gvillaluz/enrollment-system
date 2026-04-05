package com.enrollmentsystem.controllers.login;

import com.enrollmentsystem.App;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.viewmodels.login.LoginViewModel;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML private StackPane root, passwordContainer;
    @FXML private TextField usernameField, passwordTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn, eyeBtn;
    @FXML private Label errorLabel;

    private final LoginViewModel viewModel = new LoginViewModel();
    private final FontIcon eyeIcon = new FontIcon("fas-eye");
    private final ProgressIndicator indicator = new ProgressIndicator();

    @FXML
    public void initialize() {
        Platform.runLater(() -> root.requestFocus());
        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());

        setupPasswordField();

        loginBtn.setContentDisplay(ContentDisplay.TEXT_ONLY);
        loginBtn.setDefaultButton(true);
        eyeBtn.setOnAction(event -> toggleMasking());
        indicator.getStyleClass().add("spinner");
        loginBtn.setGraphic(indicator);

        setupBindings();

    }

    @FXML
    public void handleLogin() {
        viewModel.login()
            .thenAccept(userDTO -> {
                Platform.runLater(() -> {
                    if (userDTO != null) {
                        UserSession.login(userDTO);

                        if (viewModel.isDefaultPassword()) {
                            goToChangePassword();
                        } else {
                            goToDashboard();
                        }
                    }
                });
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

    private void goToChangePassword() {
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/login/ChangeDefaultPassword.fxml"));
            Parent dashboardRoot = dashboardLoader.load();

            Scene currentScene = root.getScene();
            Stage stage = (Stage) currentScene.getWindow();

            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/login/login.css")).toExternalForm()
            );

            currentScene.setRoot(dashboardRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBindings() {
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());

        viewModel.isLoadingProperty().addListener((obs, oldVal, isLoading) -> {
            if (isLoading){
                loginBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                loginBtn.addEventFilter(MouseEvent.ANY, Event::consume);
                loginBtn.addEventFilter(KeyEvent.ANY, Event::consume);
            } else {
                loginBtn.setContentDisplay(ContentDisplay.TEXT_ONLY);
                loginBtn.removeEventFilter(MouseEvent.ANY, Event::consume);
                loginBtn.removeEventFilter(KeyEvent.ANY, Event::consume);
            }
        });
    }

    private void setupPasswordField() {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        eyeIcon.setIconSize(16);
        eyeIcon.getStyleClass().add("password-eye-icon");
        eyeBtn.setGraphic(eyeIcon);

        StackPane.setAlignment(eyeBtn, javafx.geometry.Pos.CENTER_RIGHT);
        StackPane.setMargin(eyeBtn, new javafx.geometry.Insets(0, 12, 0, 0));

        eyeBtn.toFront();
    }

    private void toggleMasking() {
        boolean wasMasked = passwordField.isVisible();

        if (wasMasked) {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            eyeIcon.setIconLiteral("fas-eye-slash");
            passwordTextField.requestFocus();
            passwordTextField.selectEnd();
        } else {

            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            eyeIcon.setIconLiteral("fas-eye");
            passwordField.requestFocus();
            passwordField.selectEnd();
        }
    }
}
