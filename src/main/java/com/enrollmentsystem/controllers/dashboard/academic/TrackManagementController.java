package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.TrackFormViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.validation.ValidationSupport;
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

    private final TrackManagementViewModel viewModel = new TrackManagementViewModel();

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();

        viewModel.loadTracks();
        trackTable.setItems(viewModel.getTracks());
    }

    @FXML
    private void openAddModal() {
        loadModal(null);
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().trackIdProperty().asObject());
        codeCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

        trackTable.setFocusTraversable(false);
        trackTable.setFocusModel(null);
    }

    private void setupActionColumn() {
        Callback<TableColumn<TrackViewModel, Void>, TableCell<TrackViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<TrackViewModel, Void> call(final TableColumn<TrackViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
//                            private final Label editLbl = new Label("Edit");
//                            private final Label archiveLbl = new Label("Delete");

                            private final FontIcon editIcon = new FontIcon("fas-edit");
                            private final FontIcon deleteIcon = new FontIcon("fas-trash-alt");

                            // 2. Create Containers (HBoxes) for the labels
                            //    This allows the whole "box" to be clickable, not just the text.
                            private final HBox editBox = new HBox(editIcon);
                            private final HBox archiveBox = new HBox(deleteIcon);
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

                                editIcon.getStyleClass().add("edit-icon");
                                deleteIcon.getStyleClass().add("delete-icon");

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
            e.printStackTrace();
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

        ViewNavigator.showDeleteModal(
                (Stage) currentStage,
                onConfirmDelete
        );
    }
}