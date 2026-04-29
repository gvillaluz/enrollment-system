package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.viewmodels.enrollment.student.RecordViewModel;
import com.enrollmentsystem.viewmodels.enrollment.student.StudentRecordViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class StudentRecordsController {
    @FXML private TextField lrnField, lastNameField, firstNameField;
    @FXML private ComboBox<StrandDTO> strandDropdown;
    @FXML private ComboBox<TrackDTO> trackDropdown;
    @FXML private ComboBox<SectionDTO> sectionDropdown;
    @FXML private ComboBox<Gender> genderDropdown;
    @FXML private ComboBox<Semester> semesterDropdown;
    @FXML private ComboBox<Integer> gradeDropdown;

    @FXML private TableView<RecordViewModel> recordsTable;
    @FXML private TableColumn<RecordViewModel, String> lrnCol, lastnameCol, firstnameCol, middlenameCol, trackCol, strandCol, sectionCol;
    @FXML private TableColumn<RecordViewModel, Integer> gradeCol;
    @FXML private TableColumn<RecordViewModel, Gender> genderCol;
    @FXML private TableColumn<RecordViewModel, Semester> termCol;

    @FXML private Button refreshBtn;

    @FXML private Pagination pagination;

    private final StudentRecordViewModel viewModel = new StudentRecordViewModel();

    @FXML
    private void initialize() {
        bindFields();
        bindColumns();
        setupPagination();
        setupLoadingState();
        setupRefreshButton();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadStudentRecords(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    @FXML
    public void loadData() {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadStudentRecords(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    @FXML
    public void clearFilter() {
        viewModel.clearFields();

        lrnField.setText(null);
        lastNameField.setText(null);
        firstNameField.setText(null);
        genderDropdown.getSelectionModel().clearSelection();
        gradeDropdown.getSelectionModel().clearSelection();
        semesterDropdown.getSelectionModel().clearSelection();
        trackDropdown.getSelectionModel().clearSelection();
        strandDropdown.getSelectionModel().clearSelection();
        sectionDropdown.getSelectionModel().clearSelection();
    }

    private void bindFields() {
        lrnField.textProperty().bindBidirectional(viewModel.lrnProperty());
        lastNameField.textProperty().bindBidirectional(viewModel.lastNameProperty());
        firstNameField.textProperty().bindBidirectional(viewModel.firstNameProperty());

        genderDropdown.setItems(FXCollections.observableArrayList(Gender.values()));
        genderDropdown.valueProperty().bindBidirectional(viewModel.genderProperty());
        genderDropdown.setCellFactory(lv -> new ListCell<Gender>() {
            @Override
            protected void updateItem(Gender item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Gender>");
                } else {
                    setText(item.getGender());
                }
            }
        });
        genderDropdown.setButtonCell(new ListCell<Gender>() {
            @Override
            protected void updateItem(Gender item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Gender>");
                } else {
                    setText(item.getGender());
                }
            }
        });

        semesterDropdown.setItems(FXCollections.observableArrayList(Semester.values()));
        semesterDropdown.valueProperty().bindBidirectional(viewModel.semesterProperty());
        semesterDropdown.setCellFactory(lv -> new ListCell<Semester>() {
            @Override
            protected void updateItem(Semester item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Semester>");
                } else {
                    setText(item.getSem());
                }
            }
        });
        semesterDropdown.setButtonCell(new ListCell<Semester>() {
            @Override
            protected void updateItem(Semester item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Semester>");
                } else {
                    setText(item.getSem());
                }
            }
        });

        gradeDropdown.setItems(FXCollections.observableArrayList( 11, 12));
        gradeDropdown.setCellFactory(lv -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == 0) {
                    setText("<Grade>");
                } else {
                    setText("Grade " + item);
                }
            }
        });
        gradeDropdown.setButtonCell(new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == 0) {
                    setText("<Grade>");
                } else {
                    setText("Grade " + item);
                }
            }
        });
        gradeDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                viewModel.gradeLevelProperty().set(newVal);
            }
        });

        trackDropdown.setItems(viewModel.getTrackList());
        trackDropdown.setCellFactory(lv -> new ListCell<TrackDTO>() {
            @Override
            protected void updateItem(TrackDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Track>");
                } else {
                    setText(item.getTrackCode());
                }
            }
        });
        trackDropdown.setButtonCell(new ListCell<TrackDTO>() {
            @Override
            protected void updateItem(TrackDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Track>");
                } else {
                    setText(item.getTrackCode());
                }
            }
        });
        trackDropdown.valueProperty().addListener((obs, oldTrack, newTrack) -> {
            if (newTrack != null) {
                viewModel.trackIdProperty().set(newTrack.getTrackId());
                viewModel.trackCodeProperty().set(newTrack.getTrackCode());
            }
        });

        strandDropdown.setItems(viewModel.getStrandList());
        strandDropdown.setCellFactory(lv -> new ListCell<StrandDTO>() {
            @Override
            protected void updateItem(StrandDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Strand>");
                } else {
                    setText(item.getStrandCode());
                }
            }
        });
        strandDropdown.setButtonCell(new ListCell<StrandDTO>() {
            @Override
            protected void updateItem(StrandDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Strand>");
                } else {
                    setText(item.getStrandCode());
                }
            }
        });
        strandDropdown.valueProperty().addListener((obs, oldStrand, newStrand) -> {
            if (newStrand != null) {
                viewModel.strandIdProperty().set(newStrand.getStrandId());
                viewModel.strandCodeProperty().set(newStrand.getStrandCode());
            }
        });

        sectionDropdown.setItems(viewModel.getSectionList());
        sectionDropdown.disableProperty().bind(
                Bindings.isEmpty(viewModel.getSectionList())
        );
        sectionDropdown.setCellFactory(lv -> new ListCell<SectionDTO>() {
            @Override
            protected void updateItem(SectionDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Section>");
                } else {
                    setText(item.getSectionName());
                }
            }
        });
        sectionDropdown.setButtonCell(new ListCell<SectionDTO>() {
            @Override
            protected void updateItem(SectionDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<Section>");
                } else {
                    setText(item.getSectionName());
                }
            }
        });
        sectionDropdown.valueProperty().addListener((obs, oldSec, newSec) -> {
            if (newSec != null) {
                viewModel.sectionIdProperty().set(newSec.getSectionId());
                viewModel.sectionNameProperty().set(newSec.getSectionName());
            }
        });
    }

    private void bindColumns() {
        lrnCol.setCellValueFactory(cell -> cell.getValue().lrnProperty());
        lastnameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstnameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        middlenameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());
        genderCol.setCellValueFactory(cell -> cell.getValue().genderProperty());
        genderCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Gender gender, boolean empty) {
                super.updateItem(gender, empty);

                if (empty || gender == null) {
                    setText(null);
                } else {
                    setText(gender.getGender());
                }
            }
        });
        gradeCol.setCellValueFactory(cell -> cell.getValue().gradeLevelProperty().asObject());
        termCol.setCellValueFactory(cell -> cell.getValue().semesterProperty());
        termCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Semester semester, boolean b) {
                super.updateItem(semester, b);

                if (b || semester == null) {
                    setText(null);
                } else {
                    setText(semester.getSem());
                }
            }
        });
        trackCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());
        strandCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());
        sectionCol.setCellValueFactory(cell -> cell.getValue().sectionNameProperty());
        sectionCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                } else if (s == null || s.isBlank()) {
                    setText("Unassigned");
                } else {
                    setText(s);
                }
            }
        });
        recordsTable.setItems(viewModel.getRecords());
        recordsTable.setFocusModel(null);
        recordsTable.setFocusTraversable(false);
    }

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadStudentRecords(pageIndex);
            return recordsTable;
        });
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");

        recordsTable.placeholderProperty().bind(
                Bindings.when(viewModel.loadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );

        pagination.disableProperty().bind(viewModel.loadingProperty());
    }

    private void setupRefreshButton() {
        FontIcon refreshIcon = new FontIcon("fas-redo");
        refreshIcon.getStyleClass().add("refresh-icon");

        refreshBtn.setGraphic(refreshIcon);
        refreshBtn.setGraphicTextGap(8);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), refreshIcon);
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
