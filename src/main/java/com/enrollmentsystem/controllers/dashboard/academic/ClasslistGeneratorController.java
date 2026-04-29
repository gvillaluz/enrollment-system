package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.academic.classlist.ClasslistGeneratorViewModel;
import com.enrollmentsystem.viewmodels.academic.classlist.ClasslistRecordViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class ClasslistGeneratorController {
    @FXML private ComboBox<Integer> gradeLevelDropdown;
    @FXML private ComboBox<SectionDTO> sectionDropdown;

    @FXML private TableView<ClasslistRecordViewModel> classlistTable;
    @FXML private TableColumn<ClasslistRecordViewModel, String> lrnCol, lastNameCol, firstNameCol, middleNameCol, sectionCol;
    @FXML private TableColumn<ClasslistRecordViewModel, Integer> gradeLevelCol;

    private final ClasslistGeneratorViewModel viewModel = new ClasslistGeneratorViewModel();

    @FXML
    public void initialize() {
        setupDropdown();
        setupTable();
        setupLoadingState();
    }

    @FXML
    public void onClear(ActionEvent event) {
        gradeLevelDropdown.getSelectionModel().clearSelection();
        sectionDropdown.getSelectionModel().clearSelection();
        viewModel.clearFields();
    }

    @FXML
    public void onLoad(ActionEvent event) {
        Window currentStage = classlistTable.getScene().getWindow();

        viewModel.loadClasslist()
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            NotificationHelper.showToast(
                                    currentStage,
                                    "Classlist loaded successfully!",
                                    "success"
                            );
                        } else {
                            NotificationHelper.showToast(
                                    currentStage,
                                    "Failed to load classlist.",
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
    }

    @FXML
    public void onExport(ActionEvent event) {
        System.out.println("Export Record.");
    }

    public void setupDropdown() {
        gradeLevelDropdown.disableProperty().bind(viewModel.sectionExistsProperty().not());
        sectionDropdown.setDisable(true);

        gradeLevelDropdown.setItems(FXCollections.observableArrayList(11, 12));
        gradeLevelDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer grade, boolean empty) {
                super.updateItem(grade, empty);

                if (empty || grade == null || grade <= 0) {
                    setText("<select one>");
                } else {
                    setText(grade.toString());
                }
            }
        });

        gradeLevelDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal > 0) {
                viewModel.gradeLevelProperty().set(newVal);

                var filteredSections = viewModel.getSections().stream()
                        .filter(sec -> sec.getSectionName().contains(newVal.toString()))
                        .toList();

                sectionDropdown.setItems(FXCollections.observableArrayList(filteredSections));
                sectionDropdown.setDisable(false);
            } else {
                sectionDropdown.setDisable(true);
                sectionDropdown.getSelectionModel().clearSelection();
            }
        });

        sectionDropdown.setItems(viewModel.getSections());
        sectionDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SectionDTO section, boolean empty) {
                super.updateItem(section, empty);

                if (empty || section == null) {
                    setText("<select one>");
                } else {
                    setText(section.getSectionName());
                }
            }
        });

        sectionDropdown.setConverter(new StringConverter<SectionDTO>() {
            @Override
            public String toString(SectionDTO sectionDTO) {
                return sectionDTO == null ? "" : sectionDTO.getSectionName();
            }

            @Override
            public SectionDTO fromString(String s) {
                return null;
            }
        });

        sectionDropdown.setCellFactory(col -> new ListCell<>() {
            @Override
            protected void updateItem(SectionDTO section, boolean empty) {
                super.updateItem(section, empty);

                if (empty || section == null) {
                    setText(null);
                } else {
                    setText(section.getSectionName());
                }
            }
        });

        sectionDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                viewModel.sectionIdProperty().set(newVal.getSectionId());
            }
        });
    }

    public void setupTable() {
        lrnCol.setCellValueFactory(col -> col.getValue().lrnProperty());
        lastNameCol.setCellValueFactory(col -> col.getValue().lastNameProperty());
        firstNameCol.setCellValueFactory(col -> col.getValue().firstNameProperty());
        middleNameCol.setCellValueFactory(col -> col.getValue().middleNameProperty());
        gradeLevelCol.setCellValueFactory(col -> col.getValue().gradeLevelProperty().asObject());
        sectionCol.setCellValueFactory(col -> col.getValue().sectionNameProperty());

        classlistTable.setItems(viewModel.getClasslist());
        classlistTable.setFocusTraversable(false);
        classlistTable.setFocusModel(null);
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        classlistTable.placeholderProperty().bind(
                Bindings.when(viewModel.loadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );

        classlistTable.disableProperty().bind(viewModel.loadingProperty());
    }
}
