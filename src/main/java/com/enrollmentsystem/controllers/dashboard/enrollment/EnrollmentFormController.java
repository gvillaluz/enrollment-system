package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.enrollment.EnrollmentFormViewModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentFormController {
    @FXML private TabPane enrollmentPane;

    @FXML private TextField lrnField, lastnameField, firstnameField, middlenameField, extensionField, ageField;
    @FXML private DatePicker dateField;

    @FXML private ComboBox<String> trackDropdown, strandDropdown, sectionDropdown;
    @FXML private ComboBox<Gender> sexDropdown;

    @FXML private Button backBtn, clearBtn, nextBtn;

    private final EnrollmentFormViewModel viewModel = new EnrollmentFormViewModel();
    private final ValidationSupport validation = new ValidationSupport();

    @FXML
    public void initialize() {
        setupDropdownSelects();
        setupControlButtons();
        setupFieldsAndValidation();
    }

    @FXML
    private void exitPopup(ActionEvent event) {
        Button exitBtn = (Button) event.getSource();
        ViewNavigator.exitAddPopup(exitBtn);
    }

    @FXML
    private void nextTab(ActionEvent event) {
        int currentIndex = enrollmentPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < enrollmentPane.getTabs().size() - 1 && !validation.isInvalid()) {
            enrollmentPane.getSelectionModel().select(currentIndex + 1);
        } else {
            validation.setErrorDecorationEnabled(true);
        }
    }

    @FXML
    private void backTab(ActionEvent event) {
        int currentIndex = enrollmentPane.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            enrollmentPane.getSelectionModel().select(currentIndex - 1);
        }
    }

    @FXML
    private void clearTabFields(ActionEvent event) {
        int currentIndex = enrollmentPane.getSelectionModel().getSelectedIndex();
        switch (currentIndex) {
            case 0:
                viewModel.clearLearnerInformationFields();
                break;
            case 1:
                viewModel.clearAcademicInformationFields();
                break;
            default:
                viewModel.clearRequirements();
        }
    }

    private void setupFieldsAndValidation() {
        lrnField.textProperty().bindBidirectional(viewModel.lrnProperty());
        lastnameField.textProperty().bindBidirectional(viewModel.lastNameProperty());
        firstnameField.textProperty().bindBidirectional(viewModel.firstNameProperty());
        middlenameField.textProperty().bindBidirectional(viewModel.middleNameProperty());
        extensionField.textProperty().bindBidirectional(viewModel.extensionNameProperty());
        dateField.valueProperty().bindBidirectional(viewModel.birthDateProperty());
        ageField.textProperty().bindBidirectional(viewModel.ageProperty());

        validation.registerValidator(lrnField, Validator.combine(
                Validator.createEmptyValidator("This field is required."),
                Validator.createRegexValidator("LRN must be 12 digits", "\\d{12}", Severity.ERROR)
        ));
        validation.registerValidator(lastnameField, Validator.createEmptyValidator("Last name is required."));
        validation.registerValidator(firstnameField, Validator.createEmptyValidator("First name is required."));
        validation.registerValidator(dateField, Validator.createEmptyValidator("Birthdate is required."));
        validation.registerValidator(ageField, Validator.combine(
                Validator.createRegexValidator("Age is required.", "\\d+", Severity.ERROR),
                Validator.createEmptyValidator("Age is required.")
        ));
        validation.registerValidator(sexDropdown, Validator.createEmptyValidator("Sex is required."));

        validation.setErrorDecorationEnabled(false);
    }

    private void setupDropdownSelects() {
        trackDropdown.getItems().addAll("Academic", "Technical-Vocational-Livelihood");
        strandDropdown.getItems().addAll("Science, Technology, Engineering, Mathematics");
        sectionDropdown.setDisable(true);

        sexDropdown.valueProperty().bindBidirectional(viewModel.sexProperty());
        sexDropdown.setItems(FXCollections.observableArrayList(Gender.values()));
        sexDropdown.setConverter(new StringConverter<Gender>() {
            @Override
            public String toString(Gender gender) {
                return (gender == null) ? "" : gender.getGender();
            }

            @Override
            public Gender fromString(String s) {
                return null;
            }
        });
    }

    private void setupControlButtons() {
        enrollmentPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() == 2) {
                nextBtn.setText("Save");
            } else {
                nextBtn.setText("Next");
            }

            backBtn.setDisable(!(newVal.intValue() > 0));
        }) ;
    }
}
