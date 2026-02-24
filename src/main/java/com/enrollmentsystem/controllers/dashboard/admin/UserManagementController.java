package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.viewmodels.academic.TrackViewModel;
import com.enrollmentsystem.viewmodels.admin.UserManagementViewModel;
import com.enrollmentsystem.viewmodels.admin.UserViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class UserManagementController {
    @FXML private TextField lastNameField, firstNameField, middleNameField, usernameField, passField;
    @FXML private ComboBox<Role> roleSelect;

    @FXML private TableView<UserViewModel> userTable;
    @FXML private TableColumn<UserViewModel, Integer> idCol;
    @FXML private TableColumn<UserViewModel, String> lastNameCol, firstNameCol, middleNameCol, usernameCol, passwordCol;
    @FXML private TableColumn<UserViewModel, Role> roleCol;
    @FXML private TableColumn<UserViewModel, Void> actionCol;

    private final UserManagementViewModel viewModel = new UserManagementViewModel();
    private final UserViewModel formInstance = viewModel.getFormInstance();

    @FXML
    private void initialize() {
        bindFields();
        setupTable();
        setupActionColumn();

        viewModel.loadUsers();
        userTable.setItems(viewModel.getUserList());
    }

    private void bindFields() {
        lastNameField.textProperty().bindBidirectional(formInstance.lastNameProperty());
        firstNameField.textProperty().bindBidirectional(formInstance.firstNameProperty());
        middleNameField.textProperty().bindBidirectional(formInstance.middleNameProperty());
        usernameField.textProperty().bindBidirectional(formInstance.usernameProperty());
        passField.textProperty().bindBidirectional(formInstance.passwordProperty());

        roleSelect.valueProperty().bindBidirectional(formInstance.roleProperty());
        roleSelect.setItems(FXCollections.observableArrayList(Role.values()));
        roleSelect.setConverter(new StringConverter<Role>() {
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

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().userIdProperty().asObject());
        lastNameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstNameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        middleNameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());
        usernameCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        passwordCol.setCellValueFactory(cell -> cell.getValue().passwordProperty());
        roleCol.setCellValueFactory(cell -> cell.getValue().roleProperty());

        roleCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);

                if (empty || role == null) {
                    setText(null);
                } else {
                    setText(role.getValue());
                }
            }
        });

        userTable.setFocusModel(null);
        userTable.setFocusTraversable(false);
    }

    private void setupActionColumn() {
        Callback<TableColumn<UserViewModel, Void>, TableCell<UserViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<UserViewModel, Void> call(final TableColumn<UserViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
                            private final Label editLbl = new Label("Edit");
                            private final Label archiveLbl = new Label("Delete");

                            // 2. Create Containers (HBoxes) for the labels
                            //    This allows the whole "box" to be clickable, not just the text.
                            private final HBox editBox = new HBox(editLbl);
                            private final HBox archiveBox = new HBox(archiveLbl);
                            private final Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
                            private final HBox pane = new HBox(editBox, separator, archiveBox);

                            {
                                editBox.setAlignment(javafx.geometry.Pos.CENTER);
                                archiveBox.setAlignment(javafx.geometry.Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                HBox.setHgrow(archiveBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                archiveBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);
                                archiveBox.setMaxWidth(Double.MAX_VALUE);

                                editLbl.getStyleClass().add("btn-action");
                                archiveLbl.getStyleClass().add("btn-action");

                                pane.setSpacing(0);

                                editBox.setOnMouseClicked(event -> {
                                    UserViewModel user = getTableView().getItems().get(getIndex());
                                    handleEdit(user);
                                });

                                archiveBox.setOnMouseClicked(event -> {
                                    UserViewModel user = getTableView().getItems().get(getIndex());
                                    handleDelete(user);
                                });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                } else {
                                    setGraphic(pane);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                }
                            }
                        };
                    }
                };

        actionCol.setCellFactory(cellFactory);
    }

    private void handleEdit(UserViewModel user) {}

    private void handleDelete(UserViewModel user) {}

    @FXML
    public void saveUser() {}
}
