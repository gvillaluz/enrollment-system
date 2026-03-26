    package com.enrollmentsystem.controllers.dashboard.enrollment;

    import com.enrollmentsystem.App;
    import com.enrollmentsystem.controllers.base.BaseController;
    import com.enrollmentsystem.dtos.EnrollmentFormDTO;
    import com.enrollmentsystem.enums.EnrollmentStatus;
    import com.enrollmentsystem.enums.Semester;
    import com.enrollmentsystem.utils.NotificationHelper;
    import com.enrollmentsystem.utils.ViewNavigator;
    import com.enrollmentsystem.viewmodels.enrollment.EnrollmentSummaryViewModel;
    import com.enrollmentsystem.viewmodels.enrollment.EnrollmentViewModel;
    import javafx.animation.PauseTransition;
    import javafx.application.Platform;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.Region;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import javafx.util.Callback;
    import javafx.util.Duration;
    import org.controlsfx.control.textfield.CustomTextField;

    import java.io.IOException;
    import java.util.Objects;

    public class EnrollmentController {
        @FXML private Label title;
        @FXML private HBox searchbarContainer;
        @FXML private TableView<EnrollmentSummaryViewModel> enrollmentTable;
        @FXML private TableColumn<EnrollmentSummaryViewModel, String> lrnCol, lastnameCol, firstnameCol, middlenameCol, ayCol, trackCol, strandCol, sectionCol;
        @FXML private TableColumn<EnrollmentSummaryViewModel, Semester> termCol;
        @FXML private TableColumn<EnrollmentSummaryViewModel, Integer> gradeCol;
        @FXML private TableColumn<EnrollmentSummaryViewModel, EnrollmentStatus> statusCol;
        @FXML private TableColumn<EnrollmentSummaryViewModel, Void> actionCol;

        @FXML private Pagination pagination;

        private final EnrollmentViewModel viewModel = new EnrollmentViewModel();
        private int gradeLevel;
        private final PauseTransition searchDebounce = new PauseTransition(Duration.millis(300));

        @FXML
        public void initialize() {
            this.gradeLevel = BaseController.getCurrentGradeLevel();
            System.out.println("Grade Level: " + gradeLevel);

            title.setText("/Grade " + gradeLevel);

            setSearchBar();
            setTableColumns();
            setupActionColumn();
            setupPagination();

            viewModel.setGradeLevel(gradeLevel);
            enrollmentTable.setItems(viewModel.getEnrollmentList());
            enrollmentTable.setFocusModel(null);
            enrollmentTable.setFocusTraversable(false);
        }

        private void setSearchBar() {
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
            searchField.textProperty().bindBidirectional(viewModel.searchValueProperty());

            searchDebounce.setOnFinished(event -> {
                if (pagination.getCurrentPageIndex() == 0) {
                    viewModel.loadData(0);
                } else {
                    pagination.setCurrentPageIndex(0);
                }
            });

            viewModel.searchValueProperty().addListener((obs, oldVal, newVal) -> {
                searchDebounce.playFromStart();
            });

            Button addStudentBtn = new Button("Add New");
            addStudentBtn.getStyleClass().add("addStudentBtn");

            addStudentBtn.setOnAction(this::addStudent);

            searchbarContainer.getChildren().addAll(searchField, addStudentBtn);
            searchbarContainer.setSpacing(3);
            searchbarContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        }

        private void setTableColumns() {
            lrnCol.setCellValueFactory(cell -> cell.getValue().lrnProperty());
            lastnameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
            firstnameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
            middlenameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());
            ayCol.setCellValueFactory(cell -> cell.getValue().academicYearProperty());
            gradeCol.setCellValueFactory(cell -> cell.getValue().gradeProperty().asObject());
            termCol.setCellValueFactory(cell -> cell.getValue().termProperty());

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

            trackCol.setCellValueFactory(cell -> cell.getValue().trackProperty());
            strandCol.setCellValueFactory(cell -> cell.getValue().strandProperty());
            sectionCol.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String name, boolean empty) {
                    super.updateItem(name, empty);

                    if (empty) {
                        setText(null);
                    } else if (name == null) {
                        setText("No Section");
                    } else {
                        setText(name);
                    }
                }
            });
            sectionCol.setCellValueFactory(cell -> cell.getValue().sectionProperty());
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

            lastnameCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.13));
            firstnameCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.13));
            middlenameCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.11));
            lrnCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.10));
            trackCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.09));
            strandCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.09));
            sectionCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.08));
            statusCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.08));
            ayCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.08));
            gradeCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.06));
            termCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.06));
            actionCol.prefWidthProperty().bind(enrollmentTable.widthProperty().multiply(0.14));

            enrollmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            enrollmentTable.setFocusTraversable(false);
        }

        private void setupActionColumn() {
            Callback<TableColumn<EnrollmentSummaryViewModel, Void>, TableCell<EnrollmentSummaryViewModel, Void>> cellFactory =
                    new Callback<>() {
                        @Override
                        public TableCell<EnrollmentSummaryViewModel, Void> call(final TableColumn<EnrollmentSummaryViewModel, Void> param) {
                            return new TableCell<>() {

                                // 1. Create Labels
                                private final Label editLbl = new Label("EDIT");
                                private final Label archiveLbl = new Label("ARCHIVE");

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
                                        EnrollmentSummaryViewModel student = getTableView().getItems().get(getIndex());
                                        handleEdit(student);
                                    });

                                    archiveBox.setOnMouseClicked(event -> {
                                        EnrollmentSummaryViewModel student = getTableView().getItems().get(getIndex());
                                        handleArchive(student);
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

        private void addStudent(ActionEvent event) {
            Button btn = (Button) event.getSource();
            Stage rootWindow = (Stage) btn.getScene().getWindow();
            ViewNavigator.showModal(loadModal(null), rootWindow);
        }

        private void handleEdit(EnrollmentSummaryViewModel student) {
            System.out.println(student.lrnProperty().get());
            Stage rootWindow = (Stage) enrollmentTable.getScene().getWindow();

            viewModel.getEditData(student.enrollmentIdProperty().get())
                    .thenAccept(dto ->  {
                        Platform.runLater(() -> {
                            ViewNavigator.showModal(loadModal(dto), rootWindow);
                        });
                    })
                    .exceptionally(ex -> {
                        Platform.runLater(() -> {
                            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                            NotificationHelper.showToast(rootWindow, cause.getMessage(), "error");
                            ex.printStackTrace();
                        });
                        return null;
                    });
        }

        private void handleArchive(EnrollmentSummaryViewModel student) {
            System.out.println(student.lrnProperty().get());
        }

        private Parent loadModal(EnrollmentFormDTO dto) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/enrollment/EnrollmentForm.fxml"));
                Parent content = loader.load();

                Runnable refreshTable = () -> {
                    Platform.runLater(() -> viewModel.loadData(0));
                };

                EnrollmentFormController controller = loader.getController();
                controller.setOnSaveSuccess(refreshTable);
                controller.updateEnrollmentData(dto);
                controller.setGradeLevel(gradeLevel);

                content.getStylesheets().add(
                        Objects.requireNonNull(App.class.getResource(
                                "/com/enrollmentsystem/styles/enrollment/enrollmentform.css"
                        )).toExternalForm()
                );

                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setupPagination() {
            pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

            pagination.setPageFactory(pageIndex -> {
                viewModel.loadData(pageIndex);
                return enrollmentTable;
            });
        }
    }
