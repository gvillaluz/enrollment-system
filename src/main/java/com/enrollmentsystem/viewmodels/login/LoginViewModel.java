package com.enrollmentsystem.viewmodels.login;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.LoginDTO;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.services.AuthService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    public User login() {
        isLoading.set(true);

        if (ValidationHelper.isNullOrEmpty(username.get()) || ValidationHelper.isNullOrEmpty(password.get())) {
            errorMessage.set("All fields are required.");
            isLoading.set(false);
            return null;
        } else {
            errorMessage.set("");
        }

        try {
            var loginUser = new LoginDTO(username.get(), password.get());

            var user = _service.loginUser(loginUser);
            if (user == null) {
                System.out.println("Failed to login user. Please try again later.");
                return null;
            } else {
                username.set("");
                password.set("");
                isLoading.set(false);
                return user;
            }
        } catch (RuntimeException e) {
            errorMessage.set(e.getMessage());
            isLoading.set(false);
            return null;
        }
    }
}
