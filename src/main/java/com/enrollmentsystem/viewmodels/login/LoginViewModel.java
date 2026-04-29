package com.enrollmentsystem.viewmodels.login;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.LoginDTO;
import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.services.AuthService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.CompletableFuture;

public class LoginViewModel {
    private final AuthService _service = AppContext.getAuthService();

    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");

    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);

    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty errorMessageProperty() { return errorMessage; }
    public BooleanProperty isLoadingProperty() { return isLoading; }

    public CompletableFuture<UserDTO> login() {
        isLoading.set(true);

        if (ValidationHelper.isNullOrEmpty(username.get()) || ValidationHelper.isNullOrEmpty(password.get())) {
            errorMessage.set("All fields are required.");
            isLoading.set(false);

            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("All fields are required.")
            );
        } else {
            errorMessage.set("");
        }

        var loginUser = new LoginDTO(username.get(), password.get());

        return _service.loginUser(loginUser)
                .whenComplete((res, ex) -> {
                    Platform.runLater(() -> isLoading.set(false));

                    if (ex != null) {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                        System.out.println(cause.getMessage());

                        Platform.runLater(() -> errorMessage.set(cause.getMessage()));
                    }
                });
    }

    public boolean isDefaultPassword() {
        return password.get().equals(com.enrollmentsystem.config.AppConfig.DEFAULT_PASSWORD);
    }
}
