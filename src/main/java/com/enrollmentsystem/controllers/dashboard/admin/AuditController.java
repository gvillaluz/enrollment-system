package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.utils.DateFormatter;
import com.enrollmentsystem.utils.ModernDatePickerSkin;
import com.enrollmentsystem.viewmodels.admin.audit.AuditTrailViewModel;
import com.enrollmentsystem.viewmodels.admin.audit.AuditViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditController {
    @FXML private VBox userFilterContainer;
    @FXML private Pagination pagination;

    @FXML private ComboBox<AuditAction> actionSelect;
    @FXML private ComboBox<AuditModule> moduleSelect;
    @FXML private DatePicker dateFromPicker, dateToPicker;

    @FXML private Button clearFilterBtn, applyFilterBtn;

    @FXML private TableView<AuditViewModel> auditTable;
    @FXML private TableColumn<AuditViewModel, Integer> logIdCol;
    @FXML private TableColumn<AuditViewModel, LocalDateTime> timestampCol;
    @FXML private TableColumn<AuditViewModel, String> userCol, descriptionCol;
    @FXML private TableColumn<AuditViewModel, AuditModule> moduleCol;
    @FXML private TableColumn<AuditViewModel, AuditAction> actionCol;

    @FXML private Button refreshBtn;

    private final AuditTrailViewModel viewModel = new AuditTrailViewModel();

    @FXML
    private void initialize() {
        setupFilters();
        setupUserSearchbar();
        setupTable();
        setupPagination();
        setupLoadingState();
        setupRefreshButton();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadAuditList(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }
    @FXML
    public void clearFilters() {
        viewModel.clearFilters();
        actionSelect.getSelectionModel().clearSelection();
        actionSelect.setValue(null);
        actionSelect.setPromptText("<select one>");

        moduleSelect.getSelectionModel().clearSelection();
        moduleSelect.setValue(null);
        moduleSelect.setPromptText("<select one>");

        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadAuditList(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    @FXML
    public void applyFilters() {
        if (pagination.getCurrentPageIndex() == 0) {
            viewModel.loadAuditList(0);
        } else {
            pagination.setCurrentPageIndex(0);
        }
    }

    private void setupUserSearchbar() {
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Enter username");
        searchField.getStyleClass().add("searchbar");
        searchField.setMinWidth(Region.USE_COMPUTED_SIZE);
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

        searchField.textProperty().bindBidirectional(viewModel.usernameFilterProperty());

        userFilterContainer.getChildren().add(searchField);
    }

    private void setupFilters() {
        actionSelect.valueProperty().bindBidirectional(viewModel.actionFilterProperty());
        actionSelect.setItems(FXCollections.observableArrayList(AuditAction.values()));

        moduleSelect.valueProperty().bindBidirectional(viewModel.moduleFilterProperty());
        moduleSelect.setItems(FXCollections.observableArrayList(AuditModule.values()));

        actionSelect.setConverter(new StringConverter<AuditAction>() {
            @Override
            public String toString(AuditAction auditAction) {
                return (auditAction == null) ? null : auditAction.getValue();
            }

            @Override
            public AuditAction fromString(String s) {
                return null;
            }
        });
        actionSelect.setButtonCell(new ListCell<AuditAction>(){
            @Override
            protected void updateItem(AuditAction action, boolean empty) {
                super.updateItem(action, empty);
                if (empty || action == null) {
                    setText("<select one>");
                } else {
                    setText(action.getValue());
                }
            }
        });

        moduleSelect.setConverter(new StringConverter<AuditModule>() {
            @Override
            public String toString(AuditModule auditModule) {
                return (auditModule == null) ? null : auditModule.getValue();
            }

            @Override
            public AuditModule fromString(String s) {
                return null;
            }
        });

        moduleSelect.setButtonCell(new ListCell<AuditModule>(){
            @Override
            protected void updateItem(AuditModule module, boolean empty) {
                super.updateItem(module, empty);
                if (empty || module == null) {
                    setText("<select one>");
                } else {
                    setText(module.getValue());
                }
            }
        });

        dateFromPicker.valueProperty().bindBidirectional(viewModel.dateFromFilterProperty());
        dateToPicker.valueProperty().bindBidirectional(viewModel.dateToFilterProperty());

        dateFromPicker.setSkin(new ModernDatePickerSkin(dateFromPicker));
        dateToPicker.setSkin(new ModernDatePickerSkin(dateToPicker));
    }

    private void setupTable() {
        logIdCol.setCellValueFactory(cell -> cell.getValue().logIdProperty().asObject());
        timestampCol.setCellValueFactory(cell -> cell.getValue().timestampProperty());
        userCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

        timestampCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DateFormatter.formatAuditTimestamp(item));
                }
            }
        });

        actionCol.setCellValueFactory(cell -> cell.getValue().actionProperty());
        actionCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(AuditAction auditAction, boolean empty) {
                super.updateItem(auditAction, empty);

                if (empty || auditAction == null) {
                    setText(null);
                } else {
                    setText(auditAction.getValue());
                }
            }
        });

        moduleCol.setCellValueFactory(cell -> cell.getValue().moduleProperty());
        moduleCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(AuditModule module, boolean empty) {
                super.updateItem(module, empty);

                if (empty || module == null) {
                    setText(null);
                } else {
                    setText(module.getValue());
                }
            }
        });

        auditTable.setItems(viewModel.getAuditList());
        auditTable.setFocusTraversable(false);
        auditTable.setFocusModel(null);
    }

    private void setupPagination() {
        pagination.pageCountProperty().bind(viewModel.totalPagesProperty());

        pagination.setPageFactory(pageIndex -> {
            viewModel.loadAuditList(pageIndex);
            return auditTable;
        });
    }

    private void setupLoadingState() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.getStyleClass().add("progress-indicator");

        Label emptyLabel = new Label("No records found.");
        emptyLabel.getStyleClass().add("place-holder");

        auditTable.placeholderProperty().bind(
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
