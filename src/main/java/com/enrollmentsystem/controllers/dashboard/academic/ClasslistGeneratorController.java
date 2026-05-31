package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.viewmodels.academic.classlist.ClasslistGeneratorViewModel;
import com.enrollmentsystem.viewmodels.academic.classlist.ClasslistRecordViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class ClasslistGeneratorController {
    @FXML private ComboBox<Integer> gradeLevelDropdown;
    @FXML private ComboBox<SectionDTO> sectionDropdown;
    @FXML private Button exportBtn, refreshBtn;

    @FXML private TableView<ClasslistRecordViewModel> classlistTable;
    @FXML private TableColumn<ClasslistRecordViewModel, String> lrnCol, lastNameCol, firstNameCol, middleNameCol, sectionCol;
    @FXML private TableColumn<ClasslistRecordViewModel, Integer> gradeLevelCol;

    private final ClasslistGeneratorViewModel viewModel = new ClasslistGeneratorViewModel();

    private RotateTransition rotateTransition;

    @FXML
    public void initialize() {
        setupDropdown();
        setupTable();
        setupLoadingState();
        setupRefreshButton();

        exportBtn.disableProperty().bind(
                Bindings.isEmpty(viewModel.getClasslist())
        );
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
        Window currentStage = classlistTable.getScene().getWindow();
        Scene scene = currentStage.getScene();
        Button btn = (Button) event.getSource();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Classlist Record");
        fileChooser.setInitialFileName(sectionDropdown.getValue().getSectionName() + " - " + viewModel.schoolYearProperty().get() + ".xlsx");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel Files (*.xlsx)",
                "*.xlsx"
        );

        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showSaveDialog(btn.getScene().getWindow());

        if (selectedFile != null) {
            scene.setCursor(Cursor.WAIT);
            viewModel.exportClasslist(selectedFile)
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            if (success) {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Classlist exported successfully.",
                                        "success"
                                );
                            } else {
                                NotificationHelper.showToast(
                                        currentStage,
                                        "Failed to export classlist.",
                                        "error"
                                );
                            }
                            scene.setCursor(Cursor.DEFAULT);
                        });
                    })
                    .exceptionally(ex -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

                        Platform.runLater(() -> {
                            NotificationHelper.showToast(currentStage, cause.getMessage(), "error");
                        });
                        scene.setCursor(Cursor.DEFAULT);
                        return null;
                    });
        }
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadClasslist();
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
                viewModel.sectionNameProperty().set(newVal.getSectionName());
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
