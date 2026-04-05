package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.viewmodels.academic.SectionViewModel;
import com.enrollmentsystem.viewmodels.admin.ArchiveRecordsViewModel;
import com.enrollmentsystem.viewmodels.admin.ArchiveViewModel;
import com.enrollmentsystem.viewmodels.enrollment.EnrollmentSummaryViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Objects;

public class ArchiveController {
    @FXML private HBox searchContainer;

    @FXML private TableView<ArchiveViewModel> archiveTable;
    @FXML private TableColumn<ArchiveViewModel, String> lrnCol, lastNameCol, firstNameCol, middleNameCol, ayCol, trackCol, strandCol;
    @FXML private TableColumn<ArchiveViewModel, Semester> termCol;
    @FXML private TableColumn<ArchiveViewModel, Integer> yearLevelCol;
    @FXML private TableColumn<ArchiveViewModel, EnrollmentStatus> statusCol;
    @FXML private TableColumn<ArchiveViewModel, Void> actionCol;

    @FXML private ComboBox<SchoolYearDTO> schoolYearSelect;
    @FXML private ComboBox<Integer> gradeLevelSelect;
    @FXML private Pagination pagination;

    private final ArchiveRecordsViewModel viewModel = new ArchiveRecordsViewModel();
    private final PauseTransition searchDebounce = new PauseTransition(Duration.millis(300));

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupSearchBar();
        setupDropdownSelect();
        setupPagination();
        setupLoadingState();

        viewModel.loadSchoolYears();
        archiveTable.setItems(viewModel.getArchiveRecords());
    }

    private void setupSearchBar() {
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Enter Student Name");
        searchField.getStyleClass().add("searchbar");
        searchField.setMinWidth(236);
        searchField.setMinHeight(27);
        searchField.setPrefWidth(236);
        searchField.setMaxWidth(Region.USE_COMPUTED_SIZE);
        searchField.setMaxHeight(27);
        searchField.textProperty().bindBidirectional(viewModel.searchValueProperty());
        searchDebounce.setOnFinished(event -> {
            if (pagination.getCurrentPageIndex() == 0) {
                viewModel.loadArchives(0);
            } else {
                pagination.setCurrentPageIndex(0);
            }

            Platform.runLater(searchField::requestFocus);
        });

        viewModel.searchValueProperty().addListener((obs, oldVal, newVal) -> {
            searchDebounce.playFromStart();
        });

        ImageView searchIcon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/enrollmentsystem/assets/icons/search-icon.png")
                ))
        );
        searchIcon.setFitHeight(15);
        searchIcon.setFitWidth(15);
        StackPane iconContainer = new StackPane(searchIcon);
        iconContainer.setPadding(new javafx.geometry.Insets(0, 5, 0, 5));

        searchField.setLeft(iconContainer);

        searchContainer.getChildren().add(searchField);
    }

    private void setupTable() {
        lrnCol.setCellValueFactory(cell -> cell.getValue().lrnProperty());
        lastNameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstNameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        middleNameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());
        ayCol.setCellValueFactory(cell -> cell.getValue().schoolYearProperty());
        yearLevelCol.setCellValueFactory(cell -> cell.getValue().gradeLevelProperty().asObject());
        termCol.setCellValueFactory(cell -> cell.getValue().semesterProperty());
        trackCol.setCellValueFactory(cell -> cell.getValue().trackCodeProperty());
        strandCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());

        termCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Semester sem, boolean empty) {
                super.updateItem(sem, empty);

                if (empty || sem == null) {
                    setText(null);
                } else {
                    setText(sem.getSem());
                }
            }
        });

        lrnCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.10));
        lastNameCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.10));
        firstNameCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.09));
        middleNameCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.09));
        ayCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.06));
        yearLevelCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.08));
        termCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.08));
        trackCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.09));
        strandCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.08));
        statusCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.08)); // Now visible
        actionCol.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.12)); // Room for "RESTORE"

        archiveTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        archiveTable.setFocusTraversable(false);
        archiveTable.setFocusModel(null);
    }

    private void setupActionColumn() {
        Callback<TableColumn<ArchiveViewModel, Void>, TableCell<ArchiveViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<ArchiveViewModel, Void> call(final TableColumn<ArchiveViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
                            private final Label editLbl = new Label("RESTORE");
                            private final Label deleteLbl = new Label("DELETE");

                            // 2. Create Containers (HBoxes) for the labels
                            //    This allows the whole "box" to be clickable, not just the text.
                            private final HBox editBox = new HBox(editLbl);
                            private final HBox deleteBox = new HBox(deleteLbl);
                            private final Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
                            private final HBox pane = new HBox(editBox, separator, deleteBox);

                            {
                                editBox.setAlignment(Pos.CENTER);
                                deleteBox.setAlignment(Pos.CENTER);

                                HBox.setHgrow(editBox, javafx.scene.layout.Priority.ALWAYS);
                                editBox.setPrefWidth(1);
                                editBox.setMaxWidth(Double.MAX_VALUE);
                                deleteBox.setPrefWidth(1);
                                deleteBox.setMaxWidth(Double.MAX_VALUE);

                                editLbl.getStyleClass().add("btn-action");
                                deleteLbl.getStyleClass().add("btn-action");

                                pane.setSpacing(0);

                                ArchiveViewModel record = getTableView().getItems().get(getIndex());

                                editBox.setOnMouseClicked(event -> handleRestore(record));
                                deleteBox.setOnMouseClicked(event -> handleDelete(record));
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

    private void setupDropdownSelect() {
        schoolYearSelect.setItems(viewModel.getSchoolYearList());
        schoolYearSelect.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SchoolYearDTO sy, boolean empty) {
                super.updateItem(sy, empty);

                if (empty || sy == null) {
                    setText("School Year");
                } else {
                    setText(sy.getStartYear() + "-" + sy.getEndYear());
                }
            }
        });

        schoolYearSelect.setConverter(new StringConverter<SchoolYearDTO>() {
            @Override
            public String toString(SchoolYearDTO schoolYearDTO) {
                return (schoolYearDTO == null) ? "" : schoolYearDTO.getStartYear() + "-" + schoolYearDTO.getEndYear();
            }

            @Override
            public SchoolYearDTO fromString(String s) {
                return null;
            }
        });

        schoolYearSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                viewModel.schoolYearIdProperty().set(newVal.getSchoolYearId());
            }
        });

        gradeLevelSelect.setItems(FXCollections.observableArrayList(0, 11, 12));
        gradeLevelSelect.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null || value == 0) {
                    setText("All Grades");
                } else {
                    setText("Grade " + value);
                }
            }
        });
        gradeLevelSelect.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null || value == 0) return "All Grades";
                return "Grade " + value;
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        });

        gradeLevelSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                viewModel.gradeLevelProperty().set(newVal);
                if (pagination.getCurrentPageIndex() == 0) {
                    viewModel.loadArchives(0);
                } else {
                    pagination.setCurrentPageIndex(0);
                }
            }
        });
    }

    private void handleRestore(ArchiveViewModel record) {}

    private void handleDelete(ArchiveViewModel record) {}

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadArchives(pageIndex);
            return archiveTable;
        });
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");

        archiveTable.placeholderProperty().bind(
                Bindings.when(viewModel.isLoadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );

        pagination.disableProperty().bind(viewModel.isLoadingProperty());
    }
}
