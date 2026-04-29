package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.App;
import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.utils.filters.ModalConfig;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.admin.user.UserManagementViewModel;
import com.enrollmentsystem.viewmodels.admin.user.UserViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class UserManagementController {
    @FXML private TableView<UserViewModel> userTable;
    @FXML private TableColumn<UserViewModel, Integer> idCol;
    @FXML private TableColumn<UserViewModel, String> lastNameCol, firstNameCol, middleNameCol, usernameCol;
    @FXML private TableColumn<UserViewModel, Role> roleCol;
    @FXML private TableColumn<UserViewModel, UserStatus> statusCol;
    @FXML private TableColumn<UserViewModel, Void> actionCol;

    @FXML private Button refreshBtn;

    @FXML private Pagination pagination;

    private final UserManagementViewModel viewModel = new UserManagementViewModel();

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupPagination();
        setupRefreshButton();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadUsers(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    @FXML
    public void openAddModal() { loadModal(null); }

    private void loadModal(UserViewModel user) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/admin/UserForm.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) userTable.getScene().getWindow();

            Runnable refreshTable = () -> {
                Platform.runLater(() -> viewModel.loadUsers(0));
            };

            UserFormController controller = loader.getController();
            controller.setOnSaveSuccess(refreshTable);
            controller.setEditUser(user);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().userIdProperty().asObject());
        lastNameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstNameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        middleNameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());
        usernameCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        roleCol.setCellValueFactory(cell -> cell.getValue().roleProperty());
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

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

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(UserStatus status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status.getStatus());
                    getStyleClass().add("btn-action");
                    setAlignment(Pos.CENTER);

                    UserViewModel user = getTableView().getItems().get(getIndex());
                    boolean isCurrentUser = user.userIdProperty().get() == UserSession.getInstance().getUser().getUserId();

                    if (isCurrentUser) {
                        setDisable(true);
                        setOpacity(0.5);
                    } else {
                        setDisable(false);
                        setOpacity(1.0);
                    }

                    setOnMouseClicked(event -> {
                        handleStatusClick(user);
                    });
                }
            }
        });

        userTable.setFocusModel(null);
        userTable.setFocusTraversable(false);
        userTable.setItems(viewModel.getUserList());
    }

    private void setupActionColumn() {
        Callback<TableColumn<UserViewModel, Void>, TableCell<UserViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<UserViewModel, Void> call(final TableColumn<UserViewModel, Void> param) {
                        return new TableCell<>() {
                            private final Label editLbl = new Label("Edit");
                            private final Label resetLbl = new Label("Reset Password");

                            private final HBox editBox = new HBox(editLbl);
                            private final HBox resetBox = new HBox(resetLbl);

                            private final Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
                            private final HBox pane = new HBox(editBox, separator, resetBox);

                            {
                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                HBox.setHgrow(resetBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setAlignment(Pos.CENTER);
                                resetBox.setAlignment(Pos.CENTER);

                                editBox.setPrefWidth(1);
                                resetBox.setPrefWidth(1);

                                editBox.setMaxWidth(Double.MAX_VALUE);
                                resetBox.setMaxWidth(Double.MAX_VALUE);

                                editLbl.getStyleClass().add("btn-action");
                                resetLbl.getStyleClass().add("btn-action");

                                pane.setSpacing(0);
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

                                    UserViewModel user = getTableView().getItems().get(getIndex());
                                    boolean isCurrentUser = user.userIdProperty().get() == UserSession.getInstance().getUser().getUserId();
                                    resetBox.setDisable(isCurrentUser);

                                    editBox.setOnMouseClicked(event -> {
                                        handleEdit(user);
                                    });

                                    resetBox.setOnMouseClicked(event -> {
                                        handleResetPassword(user);
                                    });
                                }
                            }
                        };
                    }
                };

        actionCol.setCellFactory(cellFactory);
    }

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadUsers(pageIndex);
            return userTable;
        });
    }

    private void handleResetPassword(UserViewModel user) {
        Window currentStage = userTable.getScene().getWindow();
        currentStage.getScene().setCursor(Cursor.WAIT);
        Runnable onConfirmAction = () -> {
            viewModel.resetPassword(user.userIdProperty().get())
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            currentStage.getScene().setCursor(Cursor.DEFAULT);
                            if (success) {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Reset password successful.",
                                        "success"
                                );
                            } else {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Failed to reset password.",
                                        "error"
                                );
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                        Platform.runLater(() -> {
                            currentStage.getScene().setCursor(Cursor.DEFAULT);
                            NotificationHelper.showToast(currentStage, cause.getMessage(), "error");
                        });

                        return null;
                    });
        };

        ViewNavigator.showConfirmModal(
                (Stage) currentStage,
                new ModalConfig(
                        "Reset User Password",
                        "Reset password for " + user.usernameProperty().get() + "? A temporary code will be generated.",
                        onConfirmAction
                )
        );

        currentStage.getScene().setCursor(Cursor.DEFAULT);
    }

    private void handleEdit(UserViewModel user) { loadModal(user); }

    private void handleStatusClick(UserViewModel user) {
        Window currentStage = userTable.getScene().getWindow();
        currentStage.getScene().setCursor(Cursor.WAIT);
        Runnable onConfirmAction = () -> {
            viewModel.updateUserStatus(user.userIdProperty().get(), user.statusProperty().get())
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            currentStage.getScene().setCursor(Cursor.DEFAULT);
                            if (success) {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Status successfully updated.",
                                        "success"
                                );
                            } else {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Failed to update user status.",
                                        "error"
                                );
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                        Platform.runLater(() -> {
                            currentStage.getScene().setCursor(Cursor.DEFAULT);
                            NotificationHelper.showToast(currentStage, cause.getMessage(), "error");
                        });
                        return null;
                    });
        };

        String title = user.statusProperty().get() == UserStatus.INACTIVE ? "Activate User Account?" : "Deactivate User Account?";

        ViewNavigator.showConfirmModal(
                (Stage) currentStage,
                new ModalConfig(
                        title,
                        "Set " + user.usernameProperty().get() + " to Inactive? This will disable their login access.",
                        onConfirmAction
                )
        );

        currentStage.getScene().setCursor(Cursor.DEFAULT);
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        userTable.placeholderProperty().bind(
                Bindings.when(viewModel.loadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );

        pagination.disableProperty().bind(viewModel.loadingProperty());
    }

    private void setupRefreshButton() {
        FontIcon refreshIcon = new FontIcon("fas-redo");
        refreshIcon.getStyleClass().add("refresh-icon");

        refreshBtn.setGraphic(refreshIcon);
        refreshBtn.setGraphicTextGap(8);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), refreshIcon);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);

        viewModel.loadingProperty().addListener((obs, wasProcessing, isNowProcessing) -> {
            if (isNowProcessing) {
                rotateTransition.play();
            } else {
                rotateTransition.stop();
                refreshIcon.setRotate(0);
            }
        });
    }
}
