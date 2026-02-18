package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.viewmodels.enrollment.RequirementsChecklistViewModel;
import com.enrollmentsystem.viewmodels.enrollment.RequirementsSummaryViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Objects;

public class RequirementsController {
    private final RequirementsChecklistViewModel viewModel = new RequirementsChecklistViewModel();

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

    @FXML
    public void initialize() {
        setupSearchBar();
        setupTable();

        viewModel.loadSampleData();

        requirementsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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

        double checkWeight = 0.065;
        beefCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        sf9Col.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        psaCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        gmcCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        auCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        formsCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));
        alsCol.prefWidthProperty().bind(requirementsTable.widthProperty().multiply(checkWeight));

        requirementsTable.setItems(viewModel.getChecklist());
    }
}
