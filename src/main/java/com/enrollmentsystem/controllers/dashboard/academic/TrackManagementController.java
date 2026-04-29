package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.filters.ModalConfig;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.track.TrackManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.track.TrackViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class TrackManagementController {
    @FXML private TextField trackCodeField;
    @FXML private TextField descriptionField;

    @FXML private TableView<TrackViewModel> trackTable;
    @FXML private TableColumn<TrackViewModel, Integer> idCol;
    @FXML private TableColumn<TrackViewModel, String> codeCol;
    @FXML private TableColumn<TrackViewModel, String> descriptionCol;
    @FXML private TableColumn<TrackViewModel, Void> actionCol;

    @FXML private Button refreshBtn;

    private final TrackManagementViewModel viewModel = new TrackManagementViewModel();
    private RotateTransition rotateTransition;

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupRefreshButton();

        viewModel.loadTracks();
    }

    @FXML
    private void openAddModal() {
        loadModal(null);
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadTracks();
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().trackIdProperty().asObject());
        codeCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

        trackTable.setFocusTraversable(false);
        trackTable.setFocusModel(null);
        trackTable.setItems(viewModel.getTracks());
    }

    private void setupActionColumn() {
        Callback<TableColumn<TrackViewModel, Void>, TableCell<TrackViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<TrackViewModel, Void> call(final TableColumn<TrackViewModel, Void> param) {
                        return new TableCell<>() {
                            private final FontIcon editIcon = new FontIcon("fas-edit");
                            private final FontIcon deleteIcon = new FontIcon("fas-trash-alt");

                            private final HBox editBox = new HBox(editIcon);
                            private final HBox archiveBox = new HBox(deleteIcon);
                            private final Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
                            private final HBox pane = new HBox(editBox, separator, archiveBox);

                            private final Tooltip deleteTooltip = new Tooltip("Delete");
                            private final Tooltip editTooltip = new Tooltip("Edit");

                            {
                                editBox.setAlignment(javafx.geometry.Pos.CENTER);
                                archiveBox.setAlignment(javafx.geometry.Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                HBox.setHgrow(archiveBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                archiveBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);
                                archiveBox.setMaxWidth(Double.MAX_VALUE);

                                editIcon.getStyleClass().add("edit-icon");
                                deleteIcon.getStyleClass().add("delete-icon");

                                Tooltip.install(editBox, editTooltip);
                                Tooltip.install(archiveBox, deleteTooltip);

                                pane.setSpacing(0);

                                editBox.setOnMouseClicked(event -> {
                                    TrackViewModel track = getTableView().getItems().get(getIndex());
                                    handleEdit(track);
                                });

                                archiveBox.setOnMouseClicked(event -> {
                                    TrackViewModel track = getTableView().getItems().get(getIndex());
                                    handleDelete(track);
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

    private void handleEdit(TrackViewModel track) { loadModal(track); }

    private void loadModal(TrackViewModel track) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/TrackForm.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) trackTable.getScene().getWindow();

            Runnable refreshTable = viewModel::loadTracks;

            TrackFormController controller = loader.getController();
            controller.setOnSaveSuccess(refreshTable);
            controller.setViewModel(viewModel);
            controller.setEditTrack(track);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDelete(TrackViewModel track) {
        Window currentStage = trackTable.getScene().getWindow();
        Runnable onConfirmDelete = () -> {
            viewModel.deleteTrack(track)
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            if (success) {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Track successfully deleted.",
                                        "success"
                                );
                            } else {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Failed to delete track.",
                                        "error"
                                );
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                        Platform.runLater(() -> {
                            NotificationHelper.showToast(currentStage, cause.getMessage(), "error");
                        });

                       return null;
                    });
        };

        ViewNavigator.showConfirmModal(
                (Stage) currentStage,
                new ModalConfig(
                        "Delete",
                        "are you sure you want to delete this record?",
                        onConfirmDelete
                )
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