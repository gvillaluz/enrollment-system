package com.enrollmentsystem.controllers.login;

import com.enrollmentsystem.App;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.viewmodels.login.LoginViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private StackPane root;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Label errorLabel;

    private final LoginViewModel viewModel = new LoginViewModel();

    @FXML
    public void initialize() {
        Platform.runLater(() -> root.requestFocus());
        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());

        loginBtn.setContentDisplay(ContentDisplay.TEXT_ONLY);
        loginBtn.setDefaultButton(true);

        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());

        viewModel.isLoadingProperty().addListener((obs, oldVal, isLoading) -> {
            if (isLoading){
                loginBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                loginBtn.setDisable(true);
            } else {
                loginBtn.setContentDisplay(ContentDisplay.TEXT_ONLY);
                loginBtn.setDisable(false);
            }
        });
    }

    @FXML
    public void handleLogin() {
        var user = viewModel.login();

        if (user != null) {
            System.out.println("Success Login Attempt!");
            UserSession.login(user);
            goToDashboard();
        } else {
            System.out.println("Failed Login Attempt!");
        }
    }

    private void goToDashboard() {
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/core/Main.fxml"));
            Parent dashboardRoot = dashboardLoader.load();

            Scene currentScene = root.getScene();
            Stage stage = (Stage) currentScene.getWindow();

            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/main.css")).toExternalForm()
            );

            currentScene.setRoot(dashboardRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
