package com.enrollmentsystem.controllers.dashboard.admin;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.viewmodels.admin.AuditTrailViewModel;
import com.enrollmentsystem.viewmodels.admin.AuditViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditController {
    @FXML private VBox userFilterContainer;

    @FXML private ComboBox<AuditAction> actionSelect;
    @FXML private ComboBox<AuditModule> moduleSelect;

    @FXML private Button clearFilterBtn, applyFilterBtn;

    @FXML private TableView<AuditViewModel> auditTable;
    @FXML private TableColumn<AuditViewModel, Integer> logIdCol;
    @FXML private TableColumn<AuditViewModel, LocalDateTime> timestampCol;
    @FXML private TableColumn<AuditViewModel, String> userCol, descriptionCol;
    @FXML private TableColumn<AuditViewModel, AuditModule> moduleCol;
    @FXML private TableColumn<AuditViewModel, AuditAction> actionCol;

    private final AuditTrailViewModel viewModel = new AuditTrailViewModel();

    @FXML
    private void initialize() {
        setupDropdownSelects();
        setupUserSearchbar();
        setupTable();
    }

    private void setupUserSearchbar() {
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Enter Student Name");
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

        userFilterContainer.getChildren().add(searchField);
    }

    private void setupDropdownSelects() {
        actionSelect.valueProperty().bindBidirectional(viewModel.actionFilterProperty());
        actionSelect.setItems(FXCollections.observableArrayList(AuditAction.values()));

        moduleSelect.valueProperty().bindBidirectional(viewModel.moduleFilterProperty());
        moduleSelect.setItems(FXCollections.observableArrayList(AuditModule.values()));

        actionSelect.setConverter(new StringConverter<AuditAction>() {
            @Override
            public String toString(AuditAction auditAction) {
                return (auditAction == null) ? "" : auditAction.getValue();
            }

            @Override
            public AuditAction fromString(String s) {
                return null;
            }
        });

        moduleSelect.setConverter(new StringConverter<AuditModule>() {
            @Override
            public String toString(AuditModule auditModule) {
                return (auditModule == null) ? "" : auditModule.getValue();
            }

            @Override
            public AuditModule fromString(String s) {
                return null;
            }
        });
    }

    private void setupTable() {
        logIdCol.setCellValueFactory(cell -> cell.getValue().logIdProperty().asObject());
        timestampCol.setCellValueFactory(cell -> cell.getValue().timestampProperty());
        userCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        descriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

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
    }
}
