package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.viewmodels.academic.SectionViewModel;
import com.enrollmentsystem.viewmodels.admin.ArchiveViewModel;
import com.enrollmentsystem.viewmodels.enrollment.EnrollmentSummaryViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Objects;

public class ArchiveController {
    @FXML private HBox searchContainer;

    @FXML private TableView<EnrollmentSummaryViewModel> archiveTable;
    @FXML private TableColumn<EnrollmentSummaryViewModel, String> lrnCol, lastNameCol, firstNameCol, middleNameCol, ayCol, termCol, trackCol, strandCol;
    @FXML private TableColumn<EnrollmentSummaryViewModel, Integer> yearLevelCol;
    @FXML private TableColumn<EnrollmentSummaryViewModel, EnrollmentStatus> statusCol;
    @FXML private TableColumn<EnrollmentSummaryViewModel, Void> actionCol;

    private final ArchiveViewModel viewModel = new ArchiveViewModel();

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupSearchBar();

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
        ayCol.setCellValueFactory(cell -> cell.getValue().academicYearProperty());
        yearLevelCol.setCellValueFactory(cell -> cell.getValue().gradeProperty().asObject());
        termCol.setCellValueFactory(cell -> cell.getValue().termProperty());
        trackCol.setCellValueFactory(cell -> cell.getValue().trackProperty());
        strandCol.setCellValueFactory(cell -> cell.getValue().strandProperty());

        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(EnrollmentStatus status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status.getStatus());
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
        Callback<TableColumn<EnrollmentSummaryViewModel, Void>, TableCell<EnrollmentSummaryViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<EnrollmentSummaryViewModel, Void> call(final TableColumn<EnrollmentSummaryViewModel, Void> param) {
                        return new TableCell<>() {

                            // 1. Create Labels
                            private final Label editLbl = new Label("RESTORE");

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
                                    EnrollmentSummaryViewModel enrollment = getTableView().getItems().get(getIndex());
                                    handleRestore(enrollment);
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

    private void handleRestore(EnrollmentSummaryViewModel enrollment) {}
}
