package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.viewmodels.academic.TrackManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TrackManagementController {
    @FXML private TextField trackCodeField;
    @FXML private TextField descriptionField;

    @FXML private TableView<TrackViewModel> trackTable;
    @FXML private TableColumn<TrackViewModel, Integer> idCol;
    @FXML private TableColumn<TrackViewModel, String> codeCol;
    @FXML private TableColumn<TrackViewModel, String> descriptionCol;
    @FXML private TableColumn<TrackViewModel, Void> actionCol;

    private final TrackManagementViewModel viewModel = new TrackManagementViewModel();
    private final TrackViewModel formInstance = viewModel.getNewTrackForm();

    @FXML
    private void initialize() {
        trackCodeField.textProperty().bindBidirectional(formInstance.trackCodeProperty());
        descriptionField.textProperty().bindBidirectional(formInstance.descriptionProperty());

        setupTable();
        setupActionColumn();

        viewModel.loadTracks();
        trackTable.setItems(viewModel.getTracks());
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

    @FXML
    public void saveTrack() {
        viewModel.saveTrack();
    }

    private void handleEdit(TrackViewModel track) {}

    private void handleDelete(TrackViewModel track) {}
}