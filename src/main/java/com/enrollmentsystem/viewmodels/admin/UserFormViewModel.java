package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.mappers.UserMapper;
import com.enrollmentsystem.services.UserService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.beans.property.*;

import java.util.concurrent.CompletableFuture;

public class UserFormViewModel {
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<Role> role = new SimpleObjectProperty<>();

    public UserViewModel originalUser;
    private Runnable onSaveSuccess;
    public UserService _service = AppContext.getUserService();

    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public ObjectProperty<Role> roleProperty() { return role; }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { this.onSaveSuccess = onSaveSuccess; }

    public void setOriginalUser(UserViewModel user) {
        originalUser = user;

        if (originalUser != null) {
            userId.set(user.userIdProperty().get());
            lastName.set(user.lastNameProperty().get());
            firstName.set(user.firstNameProperty().get());
            middleName.set(user.middleNameProperty().get());
            username.set(user.usernameProperty().get());
            password.set(user.passwordProperty().get());
            role.set(user.roleProperty().get());
        } else {
            userId.set(0);
            lastName.set("");
            firstName.set("");
            middleName.set("");
            username.set("");
            password.set("");
            role.set(null);
        }
    }

    public CompletableFuture<Boolean> saveUser() {
        try {
            ValidationHelper.validateUserFields(lastName, firstName, username, role);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return CompletableFuture.failedFuture(e);
        }

        UserDTO userDTO = UserMapper.formToDTO(this);

        if (originalUser == null) {
            if (!userDTO.getPassword().isEmpty()) {
                if (userDTO.getPassword().length() < 8) {
                    return CompletableFuture.failedFuture(
                            new IllegalArgumentException("Password must be at least 8 characters long.")
                    );
                }
            } else {
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Password cannot be empty.")
                );
            }

            return _service.createUser(userDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        } else {
            if (ValidationHelper.hasChanges(this, originalUser))
                return CompletableFuture.completedFuture(true);

            return _service.updateUserInfo(userDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        }
    }
}
