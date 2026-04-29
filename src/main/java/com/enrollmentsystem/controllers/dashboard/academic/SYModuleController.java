package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.controllers.base.BaseController;
import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.schoolyear.SchoolYearModuleViewModel;
import com.enrollmentsystem.viewmodels.academic.schoolyear.SchoolYearViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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

public class SYModuleController {
    private final SchoolYearModuleViewModel viewModel = new SchoolYearModuleViewModel();

    @FXML private TableView<SchoolYearViewModel> schoolYearTable;
    @FXML private TableColumn<SchoolYearViewModel, Integer> academicYearIdCol;
    @FXML private TableColumn<SchoolYearViewModel, String> academicYearCol;
    @FXML private TableColumn<SchoolYearViewModel, SchoolYearStatus> statusCol;
    @FXML private TableColumn<SchoolYearViewModel, Void> actionCol;

    @FXML private Button refreshBtn;

    private RotateTransition rotateTransition;

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupLoadingState();
        setupRefreshButton();

        viewModel.loadSchoolYears();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadSchoolYears();
    }

    private void setupTable() {
        academicYearIdCol.setCellValueFactory(cell -> cell.getValue().schoolYearIdProperty().asObject());
        academicYearCol.setCellValueFactory(cell -> cell.getValue().academicYearProperty());
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(SchoolYearStatus status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status.getDbValue());
                    setAlignment(Pos.CENTER);
                    getStyleClass().add("status-link");

                    SchoolYearViewModel schoolYear = getTableView().getItems().get(getIndex());

                    boolean isOpen = schoolYear.statusProperty().get() == SchoolYearStatus.OPEN;
                    setDisable(isOpen);

                    if (isOpen) {
                        setOpacity(0.5);
                    } else {
                        setOpacity(1);
                    }

                    setOnMouseClicked(event -> {
                        loadConfirmModal(schoolYear);
                    });
                }
            }
        });

        schoolYearTable.setFocusModel(null);
        schoolYearTable.setFocusTraversable(false);
        schoolYearTable.setItems(viewModel.getSchoolYears());
    }

    @FXML
    public void openAddModal() {
        loadModal(null);
    }

    private void setupActionColumn() {
        Callback<TableColumn<SchoolYearViewModel, Void>, TableCell<SchoolYearViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<SchoolYearViewModel, Void> call(final TableColumn<SchoolYearViewModel, Void> param) {
                        return new TableCell<>() {
                            private final Label editLbl = new Label("Edit");
                            private final HBox editBox = new HBox(editLbl);

                            {
                                editBox.setAlignment(javafx.geometry.Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);

                                editLbl.getStyleClass().add("btn-action");

                                editBox.setOnMouseClicked(event -> {
                                    SchoolYearViewModel schoolYear = getTableView().getItems().get(getIndex());
                                    handleEdit(schoolYear);
                                });

                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                } else {
                                    setGraphic(editBox);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                }
                            }
                        };
                    }
                };

        actionCol.setCellFactory(cellFactory);
    }

    private void handleStatusClicked(SchoolYearViewModel schoolYear) {
        Stage currentStage = (Stage) schoolYearTable.getScene().getWindow();
        Window mainDashboard = currentStage.getOwner();

        viewModel.openSchoolYear(schoolYear)
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            NotificationHelper.showToast(mainDashboard, "School Year [" + schoolYear.academicYearProperty().get() + "] is now Open", "success");
                        } else {
                            NotificationHelper.showToast(mainDashboard, "Failed to change status. Year remains Closed.", "error");
                        }
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        NotificationHelper.showToast(mainDashboard, cause.getMessage(), "error");
                    });
                    return null;
                });
    }

    private void handleEdit(SchoolYearViewModel schoolYear) { loadModal(schoolYear); }

    private void loadModal(SchoolYearViewModel schoolYear) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/SchoolYearForm.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) schoolYearTable.getScene().getWindow();

            Runnable refreshTable = viewModel::loadSchoolYears;

            SYFormController controller = loader.getController();
            controller.setOnSaveSuccess(refreshTable);
            controller.setOriginalSy(schoolYear);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadConfirmModal(SchoolYearViewModel schoolYear) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/SchoolYearConfirmModal.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) schoolYearTable.getScene().getWindow();

            SYActivationModalController controller = loader.getController();
            controller.setOnConfirmAction(() -> handleStatusClicked(schoolYear));
            controller.setup(false);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        schoolYearTable.placeholderProperty().bind(
                Bindings.when(viewModel.loadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );
    }

    private void setupRefreshButton() {
        FontIcon refreshIcon = new FontIcon("fas-redo");
        refreshIcon.getStyleClass().add("refresh-icon");

        refreshBtn.setGraphic(refreshIcon);
        refreshBtn.setGraphicTextGap(8);

        rotateTransition = new RotateTransition(Duration.seconds(2), refreshIcon);
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
