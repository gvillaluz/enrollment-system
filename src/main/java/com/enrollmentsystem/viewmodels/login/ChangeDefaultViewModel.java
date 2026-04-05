package com.enrollmentsystem.viewmodels.login;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.services.AuthService;
import com.enrollmentsystem.services.UserService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.CompletableFuture;

public class ChangeDefaultViewModel {
    private final StringProperty newPassword = new SimpleStringProperty("");
    private final StringProperty reEnterNewPassword = new SimpleStringProperty("");
    private final StringProperty errorLabel = new SimpleStringProperty("");
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);

    public StringProperty newPasswordProperty() { return newPassword; }
    public StringProperty confirmNewPassword() { return reEnterNewPassword; }
    public StringProperty errorLabelProperty() { return errorLabel; }
    public BooleanProperty loadingProperty() { return isLoading; }

    private final AuthService _service = AppContext.getAuthService();

    public CompletableFuture<Boolean> updatePassword() {
        if (newPassword.get().length() < 8 || reEnterNewPassword.get().length() < 8 )
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Password must be at least 8 characters.")
            );

        if (!newPassword.get().matches(".*[A-Z].*") || !newPassword.get().matches(".*[0-9].*"))
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid password format.")
            );

        if (!reEnterNewPassword.get().equals(newPassword.get()))
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Confirm password must be same as the password")
            );

        return _service.updateDefaultPassword(newPassword.get());
    }
}
