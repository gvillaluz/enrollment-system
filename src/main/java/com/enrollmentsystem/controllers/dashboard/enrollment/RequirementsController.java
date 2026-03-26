package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.utils.ValidationHelper;
import com.enrollmentsystem.viewmodels.enrollment.RequirementsChecklistViewModel;
import com.enrollmentsystem.viewmodels.enrollment.RequirementsSummaryViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;

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

    @FXML private Pagination pagination;

    @FXML
    public void initialize() {
        setupSearchBar();
        setupTable();
        setupPagination();

        requirementsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        requirementsTable.setFocusTraversable(false);
        requirementsTable.setFocusModel(null);
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
    }

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadChecklist(pageIndex);
            return requirementsTable;
        });
    }
}
