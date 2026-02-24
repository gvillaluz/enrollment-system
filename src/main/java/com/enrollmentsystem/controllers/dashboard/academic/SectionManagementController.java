package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.viewmodels.academic.SectionManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.SectionViewModel;
import com.enrollmentsystem.viewmodels.academic.StrandViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class SectionManagementController {
    private final SectionManagementViewModel viewModel = new SectionManagementViewModel();

    @FXML TableView<SectionViewModel> sectionsTable;
    @FXML TableColumn<SectionViewModel, Integer> idCol, capCol;
    @FXML TableColumn<SectionViewModel, String> nameCol, syCol, strandCol, roomCol;
    @FXML TableColumn<SectionViewModel, Void> actionCol;

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();

        viewModel.loadSections();
        sectionsTable.setItems(viewModel.getSections());
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().sectionIdProperty().asObject());
        nameCol.setCellValueFactory(cell -> cell.getValue().sectionNameProperty());
        capCol.setCellValueFactory(cell -> cell.getValue().maxCapacityProperty().asObject());
        syCol.setCellValueFactory(cell -> cell.getValue().schoolYearProperty());
        strandCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());
        roomCol.setCellValueFactory(cell -> cell.getValue().roomAssignmentProperty());

        sectionsTable.setFocusTraversable(false);
        sectionsTable.setFocusModel(null);
    }

    private void setupActionColumn() {
        Callback<TableColumn<SectionViewModel, Void>, TableCell<SectionViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<SectionViewModel, Void> call(final TableColumn<SectionViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
                            private final Label editLbl = new Label("Edit");

                            // 2. Create Containers (HBoxes) for the labels
                            //    This allows the whole "box" to be clickable, not just the text.
                            private final HBox editBox = new HBox(editLbl);
                            private final HBox pane = new HBox(editBox);

                            {
                                editBox.setAlignment(javafx.geometry.Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);

                                editLbl.getStyleClass().add("btn-action");

                                pane.setSpacing(0);

                                editBox.setOnMouseClicked(event -> {
                                    SectionViewModel section = getTableView().getItems().get(getIndex());
                                    handleEdit(section);
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

    private void handleEdit(SectionViewModel section) {}

    @FXML
    public void saveTrack() {

    }
}
