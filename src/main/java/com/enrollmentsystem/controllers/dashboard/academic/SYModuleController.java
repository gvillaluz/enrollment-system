package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.viewmodels.academic.SchoolYearModuleViewModel;
import com.enrollmentsystem.viewmodels.academic.SchoolYearViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SYModuleController {
    private final SchoolYearModuleViewModel viewModel = new SchoolYearModuleViewModel();
    SchoolYearViewModel formInstance = viewModel.getFormInstance();

    @FXML private TextField startYearField, endYearField;

    @FXML private TableView<SchoolYearViewModel> schoolYearTable;
    @FXML private TableColumn<SchoolYearViewModel, Integer> academicYearIdCol;
    @FXML private TableColumn<SchoolYearViewModel, String> academicYearCol;
    @FXML private TableColumn<SchoolYearViewModel, SchoolYearStatus> statusCol;

    @FXML
    private void initialize() {
        startYearField.textProperty().bindBidirectional(formInstance.startYearProperty());
        endYearField.textProperty().bindBidirectional(formInstance.endYearProperty());

        setupTable();
        viewModel.loadSchoolYears();
        schoolYearTable.setItems(viewModel.getSchoolYears());
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

                    setOnMouseClicked(event -> {
                        viewModel.openSchoolYear();
                    });
                }
            }
        });

        schoolYearTable.setFocusModel(null);
        schoolYearTable.setFocusTraversable(false);
    }

    @FXML
    public void saveYear() {
        viewModel.addSchoolYear();
    }
}
