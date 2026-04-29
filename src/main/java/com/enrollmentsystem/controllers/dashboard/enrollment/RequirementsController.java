package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.enrollment.requirements.NoteViewModel;
import com.enrollmentsystem.viewmodels.enrollment.requirements.RequirementsChecklistViewModel;
import com.enrollmentsystem.viewmodels.enrollment.requirements.RequirementsSummaryViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RequirementsController {
    private final RequirementsChecklistViewModel viewModel = new RequirementsChecklistViewModel();
    private final PauseTransition searchDebounce = new PauseTransition(Duration.millis(300));

    @FXML private HBox searchContainer;
    @FXML private TableView<RequirementsSummaryViewModel> requirementsTable;
    @FXML private TableColumn<RequirementsSummaryViewModel, String> lrnCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, String> lastnameCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, String> firstnameCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, String> middlenameCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> beefCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> sf9Col;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> psaCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> gmcCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> auCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> formsCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Boolean> alsCol;
    @FXML private TableColumn<RequirementsSummaryViewModel, Void> noteCol;

    @FXML private Button refreshBtn;

    @FXML private Pagination pagination;

    @FXML
    public void initialize() {
        setupSearchBar();
        setupTable();
        setupPagination();
        setupNoteColumn();
        setupRefreshButton();
        setupLoadingState();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadChecklist(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    private void setupSearchBar() {
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Enter Student Name");
        searchField.getStyleClass().add("searchbar");
        searchField.setMinWidth(432);
        searchField.setMinHeight(27);
        searchField.setPrefWidth(236);
        searchField.setMaxWidth(Region.USE_COMPUTED_SIZE);
        searchField.setMaxHeight(27);
        searchField.textProperty().bindBidirectional(viewModel.searchValueProperty());

        searchDebounce.setOnFinished(event -> {
            if (pagination.getCurrentPageIndex() == 0) {
                viewModel.loadChecklist(0);
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
        lastnameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstnameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        middlenameCol.setCellValueFactory(cell -> cell.getValue().middleNameProperty());

        beefCol.setCellValueFactory(cell -> cell.getValue().beefProperty());
        beefCol.setCellFactory(CheckBoxTableCell.forTableColumn(beefCol));

        sf9Col.setCellValueFactory(cell -> cell.getValue().sf9Property());
        sf9Col.setCellFactory(CheckBoxTableCell.forTableColumn(sf9Col));

        psaCol.setCellValueFactory(cell -> cell.getValue().psaProperty());
        psaCol.setCellFactory(CheckBoxTableCell.forTableColumn(psaCol));

        gmcCol.setCellValueFactory(cell -> cell.getValue().gmcProperty());
        gmcCol.setCellFactory(CheckBoxTableCell.forTableColumn(gmcCol));

        auCol.setCellValueFactory(cell -> cell.getValue().auProperty());
        auCol.setCellFactory(CheckBoxTableCell.forTableColumn(auCol));

        formsCol.setCellValueFactory(cell -> cell.getValue().form5Property());
        formsCol.setCellFactory(CheckBoxTableCell.forTableColumn(formsCol));

        alsCol.setCellValueFactory(cell -> cell.getValue().alsCocProperty());
        alsCol.setCellFactory(CheckBoxTableCell.forTableColumn(alsCol));

        lrnCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(0.12));
        lastnameCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(0.12));
        firstnameCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(0.12));
        middlenameCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(0.12));
        requirementsTable.setItems(viewModel.getChecklist());
        requirementsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        requirementsTable.setFocusTraversable(false);
        requirementsTable.setFocusModel(null);
    }

    private void setupNoteColumn() {
        Callback<TableColumn<RequirementsSummaryViewModel, Void>, TableCell<RequirementsSummaryViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<RequirementsSummaryViewModel, Void> call(final TableColumn<RequirementsSummaryViewModel, Void> param) {
                        return new TableCell<>() {
                            private final Label countLabel = new Label();
                            private final StackPane badgePane = new StackPane();
                            private final FontIcon noteIcon = new FontIcon("fas-comment-alt");
                            private final HBox noteBox = new HBox(noteIcon);

                            {
                                countLabel.getStyleClass().add("badge-count");
                                badgePane.getStyleClass().add("badge");

                                Circle badgeBackground = new Circle(7, javafx.scene.paint.Color.RED);

                                badgePane.getChildren().addAll(badgeBackground, countLabel);
                                StackPane stack = new StackPane(noteIcon, badgePane);
                                noteBox.getChildren().add(stack);
                                noteBox.setAlignment(javafx.geometry.Pos.CENTER);

                                noteIcon.setOnMouseClicked(event -> {
                                    RequirementsSummaryViewModel req = getTableView().getItems().get(getIndex());
                                    showNotes(req);
                                });

                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                } else {
                                    RequirementsSummaryViewModel req = getTableView().getItems().get(getIndex());
                                    int count = req.noteCountProperty().get();

                                    req.noteCountProperty().removeListener((obs, old, val) -> {});

                                    req.noteCountProperty().addListener((obs, oldVal, newVal) -> {
                                        Platform.runLater(() -> {
                                            int currentCount = newVal.intValue();
                                            if (currentCount > 0) {
                                                countLabel.setText(String.valueOf(currentCount));
                                                badgePane.setVisible(true);
                                            } else {
                                                badgePane.setVisible(false);
                                            }
                                        });
                                    });

                                    if (count > 0) {
                                        countLabel.setText(String.valueOf(count));
                                        badgePane.setVisible(true);
                                    } else {
                                        badgePane.setVisible(false);
                                    }
                                    setGraphic(noteBox);
                                    setPadding(javafx.geometry.Insets.EMPTY);
                                }
                            }
                        };
                    }
                };

        noteCol.setCellFactory(cellFactory);
    }

    private void showNotes(RequirementsSummaryViewModel req) {
        Stage rootWindow = (Stage) requirementsTable.getScene().getWindow();
        Scene rootScene = (Scene) requirementsTable.getScene();

        rootScene.setCursor(Cursor.WAIT);

        viewModel.getRequirementNotes(req.lrnProperty().get())
                .thenAccept(notes -> {
                    Platform.runLater(() -> {
                        rootScene.setCursor(Cursor.DEFAULT);
                        ViewNavigator.showModal(loadNoteModal(notes, req.lrnProperty().get()), rootWindow);
                    });
                })
                .exceptionally(ex -> {
                    rootScene.setCursor(Cursor.DEFAULT);
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    NotificationHelper.showToast(rootWindow, cause.getMessage(), "error");
                    System.out.println(ex.getMessage());

                    return null;
                });
    }

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadChecklist(pageIndex);
            return requirementsTable;
        });
    }

    public Parent loadNoteModal(List<NoteViewModel> notes, String lrn) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/enrollment/RequirementNoteModal.fxml"));
            Parent content = loader.load();

            RequirementNoteController controller = loader.getController();
            controller.setNotes(notes, lrn);
            controller.setOnCloseCallback(newCount -> {
                System.out.println(newCount);
                requirementsTable.getItems().stream()
                        .filter(n -> n.lrnProperty().get().equals(lrn))
                        .findFirst()
                        .ifPresent(req -> req.noteCountProperty().set(newCount));
            });

            content.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource(
                            "/com/enrollmentsystem/styles/enrollment/requirementnote.css"
                    )).toExternalForm()
            );

            return content;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        requirementsTable.placeholderProperty().bind(
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
