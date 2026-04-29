package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.filters.ModalConfig;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.strand.StrandManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.strand.StrandViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class StrandManagementController {
    @FXML private TableView<StrandViewModel> strandTable;
    @FXML private TableColumn<StrandViewModel, Integer> idCol;
    @FXML private TableColumn<StrandViewModel, String> strandCodeCol, descriptionCol, trackCodeCol;
    @FXML private TableColumn<StrandViewModel, Void> actionCol;

    @FXML private Button refreshBtn;

    private final StrandManagementViewModel viewModel = new StrandManagementViewModel();
    private RotateTransition rotateTransition;

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupRefreshButton();
        setupLoadingState();

        viewModel.loadStrands();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadStrands();
    }

    @FXML
    private void openAddModal() { loadModal(null); }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().strandIdProperty().asObject());
        strandCodeCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        trackCodeCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());

        strandTable.setFocusTraversable(false);
        strandTable.setFocusModel(null);
        strandTable.setItems(viewModel.getStrands());
    }

    private void setupActionColumn() {
        Callback<TableColumn<StrandViewModel, Void>, TableCell<StrandViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<StrandViewModel, Void> call(final TableColumn<StrandViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
                            private final FontIcon editIcon = new FontIcon("fas-edit");
                            private final FontIcon deleteIcon = new FontIcon("fas-trash-alt");

                            // 2. Create Containers (HBoxes) for the labels
                            //    This allows the whole "box" to be clickable, not just the text.
                            private final HBox editBox = new HBox(editIcon);
                            private final HBox archiveBox = new HBox(deleteIcon);
                            private final Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
                            private final HBox pane = new HBox(editBox, separator, archiveBox);

                            private final Tooltip editTooltip = new Tooltip("Edit");
                            private final Tooltip deleteTooltip = new Tooltip("Delete");

                            {
                                editBox.setAlignment(javafx.geometry.Pos.CENTER);
                                archiveBox.setAlignment(javafx.geometry.Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                HBox.setHgrow(archiveBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                archiveBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);
                                archiveBox.setMaxWidth(Double.MAX_VALUE);

                                editIcon.getStyleClass().add("btn-action");
                                deleteIcon.getStyleClass().add("btn-action");

                                Tooltip.install(editBox, editTooltip);
                                Tooltip.install(archiveBox, deleteTooltip);

                                pane.setSpacing(0);

                                editBox.setOnMouseClicked(event -> {
                                    StrandViewModel strand = getTableView().getItems().get(getIndex());
                                    handleEdit(strand);
                                });

                                archiveBox.setOnMouseClicked(event -> {
                                    StrandViewModel strand = getTableView().getItems().get(getIndex());
                                    handleDelete(strand);
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

    @FXML private void handleEdit(StrandViewModel strand) { loadModal(strand); }

    private void loadModal(StrandViewModel strand) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/StrandForm.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) strandTable.getScene().getWindow();

            Runnable refreshTable = viewModel::loadStrands;

            StrandFormController controller = loader.getController();
            controller.setOnSaveSuccess(refreshTable);
            controller.setEditStrand(strand);

            if (strand != null) controller.setEditStrand(strand);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML private void handleDelete(StrandViewModel strand) {
        Window currentStage = strandTable.getScene().getWindow();
        Runnable onConfirmDelete = () -> {
            viewModel.deleteStrand(strand)
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            if (success) {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Strand successfully deleted.",
                                        "success"
                                );
                            } else {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Failed to delete strand.",
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

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        strandTable.placeholderProperty().bind(
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
