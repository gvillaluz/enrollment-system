package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.viewmodels.academic.StrandManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.StrandViewModel;
import com.enrollmentsystem.viewmodels.academic.TrackViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class StrandManagementController {
    @FXML private ComboBox<String> trackCodeSelect;
    @FXML private TextField strandCodeField, descriptionField;

    @FXML private TableView<StrandViewModel> strandTable;
    @FXML private TableColumn<StrandViewModel, Integer> idCol;
    @FXML private TableColumn<StrandViewModel, String> strandCodeCol, descriptionCol, trackCodeCol;
    @FXML private TableColumn<StrandViewModel, Void> actionCol;

    private final StrandManagementViewModel viewModel = new StrandManagementViewModel();
    StrandViewModel formInstance = viewModel.getFormViewModel();

    @FXML
    private void initialize() {
        //trackCodeSelect.valueProperty().bindBidirectional(formInstance.trackCodeProperty());
        strandCodeField.textProperty().bindBidirectional(formInstance.strandCodeProperty());
        descriptionField.textProperty().bindBidirectional(formInstance.descriptionProperty());

        trackCodeSelect.setValue(null);

        setupTable();
        setupActionColumn();

        viewModel.loadStrands();
        strandTable.setItems(viewModel.getStrands());
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().strandIdProperty().asObject());
        strandCodeCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        trackCodeCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());

        strandTable.setFocusTraversable(false);
        strandTable.setFocusModel(null);
    }

    private void setupActionColumn() {
        Callback<TableColumn<StrandViewModel, Void>, TableCell<StrandViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<StrandViewModel, Void> call(final TableColumn<StrandViewModel, Void> param) {
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

    @FXML public void saveStrand() {
        viewModel.saveStrand();
    }

    @FXML private void handleEdit(StrandViewModel strand) {}

    @FXML private void handleDelete(StrandViewModel strand) {}
}
