package com.enrollmentsystem.controllers.dashboard.academic;

import com.enrollmentsystem.App;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.academic.section.SectionManagementViewModel;
import com.enrollmentsystem.viewmodels.academic.section.SectionViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class SectionManagementController {
    private final SectionManagementViewModel viewModel = new SectionManagementViewModel();

    @FXML TableView<SectionViewModel> sectionsTable;
    @FXML TableColumn<SectionViewModel, Integer> idCol;
    @FXML TableColumn<SectionViewModel, String> nameCol, syCol, strandCol, roomCol;
    @FXML TableColumn<SectionViewModel, Void> actionCol;

    @FXML private Button refreshBtn;

    private RotateTransition rotateTransition;

    @FXML
    private void initialize() {
        setupTable();
        setupActionColumn();
        setupLoadingState();
        setupRefreshButton();

        viewModel.loadSections();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadSections();
    }

    private void setupTable() {
        idCol.setCellValueFactory(cell -> cell.getValue().sectionIdProperty().asObject());
        nameCol.setCellValueFactory(cell -> cell.getValue().sectionNameProperty());
        syCol.setCellValueFactory(cell -> cell.getValue().schoolYearProperty());
        strandCol.setCellValueFactory(cell -> cell.getValue().strandCodeProperty());
        roomCol.setCellValueFactory(cell -> cell.getValue().roomAssignmentProperty());

        sectionsTable.setFocusTraversable(false);
        sectionsTable.setFocusModel(null);
        sectionsTable.setItems(viewModel.getSections());
    }

    private void setupActionColumn() {
        Callback<TableColumn<SectionViewModel, Void>, TableCell<SectionViewModel, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<SectionViewModel, Void> call(final TableColumn<SectionViewModel, Void> param) {
                        return new TableCell<>() {
                            private final Label editLbl = new Label("Edit");
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
                                    SectionViewModel section = getTableView().getItems().get(getIndex());
                                    handleEdit(section);
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

    private void handleEdit(SectionViewModel section) { loadModal(section); }

    private void loadModal(SectionViewModel section) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/academic/SectionForm.fxml"));
            Parent modalContent = loader.load();
            Stage owner = (Stage) sectionsTable.getScene().getWindow();

            Runnable refreshTable = viewModel::loadSections;

            SectionFormController controller = loader.getController();
            controller.setOnSaveSuccess(refreshTable);
            controller.setOriginalSection(section);

            modalContent.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/form.css")).toExternalForm()
            );

            ViewNavigator.showModal(modalContent, owner);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No sections found for this School Year. Please go to 'Run Batch Sectioning' to generate sections based on student enrollment.");
        emptyLabel.getStyleClass().add("place-holder");

        sectionsTable.placeholderProperty().bind(
                Bindings.when(viewModel.loadingProperty())
                        .then((Node) progressIndicator)
                        .otherwise((Node) emptyLabel)
        );
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
