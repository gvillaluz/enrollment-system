package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.admin.UserFormViewModel;
import com.enrollmentsystem.viewmodels.admin.UserViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;


public class UserFormController {
    @FXML private Label titleLabel;
    @FXML private VBox modal;
    @FXML private HBox passwordContainer;
    @FXML private TextField lastNameField, firstNameField, middleNameField, usernameField, passwordField;
    @FXML private ComboBox<Role> roleField;

    private final UserFormViewModel formViewModel = new UserFormViewModel();

    @FXML
    public void initialize() {
        bindFields();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { formViewModel.setOnSaveSuccess(onSaveSuccess); }

    public void setEditUser(UserViewModel editUser) {
        formViewModel.setOriginalUser(editUser);

        if (editUser == null) {
            passwordContainer.setVisible(true);
            passwordContainer.setManaged(true);
            titleLabel.setText("Add New User");
        } else {
            passwordContainer.setVisible(false);
            passwordContainer.setManaged(false);
            titleLabel.setText("Edit User");
        }
    }

    @FXML
    public void onCancel(ActionEvent event) { closeModal(event); }

    @FXML
    public void onSave(ActionEvent event) {
        Stage currentStage = (Stage) modal.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        String successMessage = formViewModel.originalUser != null ? "User successfully updated." : "User successfully added.";
        String errorMessage = formViewModel.originalUser != null ? "Failed to update user." : "Failed to add user.";

        formViewModel.saveUser()
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            currentStage.close();
                            NotificationHelper.showToast(mainDashboard, successMessage, "success");
                        } else {
                            NotificationHelper.showToast(mainDashboard, errorMessage, "error");
                        }
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        NotificationHelper.showToast(mainDashboard, cause.getMessage(), "error");
                        ex.printStackTrace();
                    });
                    return null;
                });
    }

    private void closeModal(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void bindFields() {
        lastNameField.textProperty().bindBidirectional(formViewModel.lastNameProperty());
        firstNameField.textProperty().bindBidirectional(formViewModel.firstNameProperty());
        middleNameField.textProperty().bindBidirectional(formViewModel.middleNameProperty());
        usernameField.textProperty().bindBidirectional(formViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(formViewModel.passwordProperty());

        roleField.valueProperty().bindBidirectional(formViewModel.roleProperty());
        roleField.setItems(FXCollections.observableArrayList(Role.values()));
        roleField.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return (role == null) ? "" : role.getValue();
            }

            @Override
            public Role fromString(String s) {
                return null;
            }
        });
    }
}
