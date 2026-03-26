package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.utils.Formatter;
import com.enrollmentsystem.utils.ModernDatePickerSkin;
import com.enrollmentsystem.utils.NotificationHelper;
import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.enrollment.EnrollmentFormViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentFormController {
    @FXML private TabPane enrollmentPane;
    @FXML private Tab learnerTab, academicTab, requirementTab;

    @FXML private TextField lrnField, lastnameField, firstnameField, middlenameField, extensionField, ageField, lastGradeLvlField, lastSyField, lastSchoolNameField, lastSchoolIdField;
    @FXML private DatePicker dateField;
    @FXML private Label semErrorLabel;

    @FXML private ComboBox<TrackDTO> trackDropdown;
    @FXML private ComboBox<StrandDTO> strandDropdown;
    @FXML private ComboBox<SectionDTO> sectionDropdown;

    @FXML private ComboBox<Gender> sexDropdown;
    @FXML private ToggleGroup semesterGroup;
    @FXML private RadioButton firstRadio, secondRadio;
    @FXML private HBox radioContainer;

    @FXML private CheckBox beefCheck, sf9Check, psaCheck, goodMoralCheck, affidavitCheck, form5Check, alsCheck;

    @FXML private Button backBtn, clearBtn, nextBtn;

    private final EnrollmentFormViewModel viewModel = new EnrollmentFormViewModel();
    private final ValidationSupport learnerValidation = new ValidationSupport();
    private final ValidationSupport academicValidation = new ValidationSupport();

    @FXML
    public void initialize() {
        setupDropdownSelects();
        setupControlButtons();
        setupFieldsAndValidation();
        setupRadioButtons();
        setupCheckboxes();
        setupEditModeAutoSelect();

        academicTab.setDisable(true);
        requirementTab.setDisable(true);
    }

    @FXML
    private void exitPopup(ActionEvent event) {
        Button exitBtn = (Button) event.getSource();
        ViewNavigator.exitAddPopup(exitBtn);
    }

    @FXML
    private void nextTab(ActionEvent event) {
        int currentIndex = enrollmentPane.getSelectionModel().getSelectedIndex();

        if (currentIndex == 0) {
            learnerValidation.redecorate();
            learnerValidation.setErrorDecorationEnabled(true);

            if (!learnerValidation.isInvalid()) {
                academicTab.setDisable(false);
                enrollmentPane.getSelectionModel().select(currentIndex + 1);
            }
        } else if (currentIndex == 1) {
            academicValidation.redecorate();
            academicValidation.setErrorDecorationEnabled(true);

            boolean isSemEmpty = viewModel.getAcademicInformationVM().isSemEmpty();

            semErrorLabel.setVisible(isSemEmpty);

            if (!academicValidation.isInvalid() && !isSemEmpty) {
                requirementTab.setDisable(false);
                enrollmentPane.getSelectionModel().select(currentIndex + 1);
            }
        } else {
            Stage currentStage = (Stage) enrollmentPane.getScene().getWindow();
            Window mainDashboard = currentStage.getOwner();

            String successMessage = viewModel.isEditing ? "Enrollment successfully updated." : "Enrollment successfully added.";
            String errorMessage = viewModel.isEditing ? "Failed to update enrollment." : "Failed to add enrollment.";

            viewModel.saveEnrollment()
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            if (success) {
                                currentStage.close();
                                NotificationHelper.showToast(mainDashboard, successMessage, "success");
                            } else {
                                NotificationHelper.showToast(mainDashboard, errorMessage, "success");
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        Platform.runLater(() -> {
                            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                            NotificationHelper.showToast(mainDashboard, cause.getMessage(), "error");
                            ex.printStackTrace();
                        });
                        return null;
                    });
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
                viewModel.getLearnerInformationVM().clear();
                learnerValidation.setErrorDecorationEnabled(false);
                sexDropdown.setPromptText("<select one>");
                break;
            case 1:
                viewModel.getAcademicInformationVM().clear();

                trackDropdown.setValue(null);
                trackDropdown.getSelectionModel().clearSelection();
                trackDropdown.setPromptText("<select track>");

                strandDropdown.setValue(null);
                strandDropdown.getSelectionModel().clearSelection();
                strandDropdown.setDisable(true);
                strandDropdown.setPromptText("<select strand>");

                sectionDropdown.setValue(null);
                sectionDropdown.getSelectionModel().clearSelection();
                sectionDropdown.setDisable(true);
                sectionDropdown.setPromptText("<select section>");

                if (semesterGroup.getSelectedToggle() != null) {
                    semesterGroup.getSelectedToggle().setSelected(false);
                }

                academicValidation.setErrorDecorationEnabled(false);
                break;
            default:
                viewModel.clearRequirements();
        }
    }

    public void setGradeLevel(int gradeLevel) { viewModel.setGradeLevel(gradeLevel); }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        viewModel.setOnSaveSuccess(onSaveSuccess);
    }

    public void updateEnrollmentData(EnrollmentFormDTO dto) {
        viewModel.setEditData(dto);
    }

    private void setupFieldsAndValidation() {
        Platform.runLater(() -> {
            lrnField.setDisable(viewModel.isEditing);
        });

        PauseTransition debounce = new PauseTransition(Duration.millis(500));

        lrnField.textProperty().addListener((obs, oldVal, newVal) -> {
            debounce.stop();

            if (newVal != null && newVal.length() == 12) {
                debounce.setOnFinished(event -> {
                    viewModel.getStudentInfoByLRN(newVal);
                });

                debounce.playFromStart();
            }
        });

        lrnField.textProperty().bindBidirectional(viewModel.getLearnerInformationVM().lrnProperty());
        lastnameField.textProperty().bindBidirectional(viewModel.getLearnerInformationVM().lastNameProperty());
        firstnameField.textProperty().bindBidirectional(viewModel.getLearnerInformationVM().firstNameProperty());
        middlenameField.textProperty().bindBidirectional(viewModel.getLearnerInformationVM().middleNameProperty());
        extensionField.textProperty().bindBidirectional(viewModel.getLearnerInformationVM().extensionNameProperty());
        dateField.valueProperty().bindBidirectional(viewModel.getLearnerInformationVM().birthDateProperty());

        dateField.setSkin(new ModernDatePickerSkin(dateField));

        TextFormatter<Integer> ageFormatter = Formatter.formatStringToInteger(null);
        TextFormatter<Integer> lastGrLvlFormatter = Formatter.formatStringToInteger(null);
        ageField.setTextFormatter(ageFormatter);
        lastGradeLvlField.setTextFormatter(lastGrLvlFormatter);

        ageFormatter.valueProperty().bindBidirectional(viewModel.getLearnerInformationVM().ageProperty());
        lastGrLvlFormatter.valueProperty().bindBidirectional(viewModel.getAcademicInformationVM().lastGrLevelProperty());

        lastSyField.textProperty().bindBidirectional(viewModel.getAcademicInformationVM().lastSchoolYearProperty());
        lastSchoolNameField.textProperty().bindBidirectional(viewModel.getAcademicInformationVM().lastSchoolAttendedProperty());
        lastSchoolIdField.textProperty().bindBidirectional(viewModel.getAcademicInformationVM().lastSchoolIdProperty());

        learnerValidation.registerValidator(lrnField, Validator.combine(
                Validator.createEmptyValidator("This field is required."),
                Validator.createRegexValidator("LRN must be 12 digits", "\\d{12}", Severity.ERROR)
        ));
        learnerValidation.registerValidator(lastnameField, Validator.createEmptyValidator("Last name is required."));
        learnerValidation.registerValidator(firstnameField, Validator.createEmptyValidator("First name is required."));
        learnerValidation.registerValidator(dateField, Validator.createEmptyValidator("Birthdate is required."));
        learnerValidation.registerValidator(ageField, Validator.combine(
                Validator.createRegexValidator("Age must be a digit.", "\\d+", Severity.ERROR),
                Validator.createEmptyValidator("Age is required.")
        ));
        learnerValidation.registerValidator(sexDropdown, Validator.createEmptyValidator("Sex is required."));

        learnerValidation.setErrorDecorationEnabled(false);
        learnerValidation.initInitialDecoration();
    }

    private void setupDropdownSelects() {
        viewModel.loadTracksAndStrands();

        strandDropdown.setDisable(true);
        sectionDropdown.setDisable(true);

        trackDropdown.setItems(viewModel.getTracks());
        trackDropdown.setConverter(new StringConverter<TrackDTO>() {
            @Override
            public String toString(TrackDTO trackDTO) {
                return (trackDTO == null) ? "" : trackDTO.getTrackCode();
            }

            @Override
            public TrackDTO fromString(String s) {
                return null;
            }
        });

        trackDropdown.valueProperty().addListener((obs, oldTrack, newTrack) -> {
            if (newTrack != null) {
                strandDropdown.setDisable(false);
                viewModel.getAcademicInformationVM().trackIdProperty().set(newTrack.getTrackId());

                var filteredStrands = viewModel.getStrandsByTrackId(newTrack.getTrackId());
                strandDropdown.setItems(FXCollections.observableArrayList(filteredStrands));
                strandDropdown.getSelectionModel().clearSelection();
                strandDropdown.setValue(null);
                strandDropdown.setPromptText("<select strand>");
            } else {
                strandDropdown.setDisable(true);
            }
        });

        viewModel.getAcademicInformationVM().trackIdProperty().addListener((obs, oldId, newId) -> {
            if (newId != null && newId.intValue() > 0 && !trackDropdown.getItems().isEmpty()) {
                trackDropdown.getItems().stream()
                        .filter(t -> t.getTrackId() == newId.intValue())
                        .findFirst()
                        .ifPresent(t -> trackDropdown.setValue(t));
            }
        });

        trackDropdown.setButtonCell(new ListCell<TrackDTO>() {
            @Override
            protected void updateItem(TrackDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<select track>");
                } else {
                    setText(item.getTrackCode());
                }
            }
        });

        strandDropdown.valueProperty().addListener((obs, oldStrand, newStrand) -> {
            if (newStrand != null) {
                sectionDropdown.setDisable(false);
                viewModel.getAcademicInformationVM().strandIdProperty().set(newStrand.getStrandId());

                var filteredSections = viewModel.getSectionsByStrandId(newStrand.getStrandId());

                if (filteredSections.isEmpty()) {
                    sectionDropdown.setDisable(true);
                }

                sectionDropdown.setItems(FXCollections.observableArrayList(filteredSections));
                sectionDropdown.getSelectionModel().clearSelection();
                sectionDropdown.setValue(null);
                sectionDropdown.setPromptText("<select section>");
            }
        });

        viewModel.getAcademicInformationVM().strandIdProperty().addListener((obs, oldId, newId) -> {
            if (newId != null && newId.intValue() > 0 && !strandDropdown.getItems().isEmpty()) {
                strandDropdown.getItems().stream()
                        .filter(s -> s.getStrandId() == newId.intValue())
                        .findFirst()
                        .ifPresent(s -> strandDropdown.setValue(s));
            }
        });

        strandDropdown.setButtonCell(new ListCell<StrandDTO>() {
            @Override
            protected void updateItem(StrandDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<select strand>");
                } else {
                    setText(item.getStrandCode());
                }
            }
        });

        strandDropdown.setConverter(new StringConverter<StrandDTO>() {
            @Override
            public String toString(StrandDTO strandDTO) {
                return (strandDTO == null) ? "" : strandDTO.getStrandCode();
            }

            @Override
            public StrandDTO fromString(String s) {
                return null;
            }
        });

        sectionDropdown.valueProperty().addListener((obs, oldSec, newSec) -> {
            if (newSec != null) {
                viewModel.getAcademicInformationVM().sectionIdProperty().set(newSec.getSectionId());
            }
        });

        sectionDropdown.setButtonCell(new ListCell<SectionDTO>() {
            @Override
            protected void updateItem(SectionDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("<select section>");
                } else {
                    setText(item.getSectionName());
                }
            }
        });

        sectionDropdown.setConverter(new StringConverter<SectionDTO>() {
            @Override
            public String toString(SectionDTO sectionDTO) {
                return (sectionDTO == null) ? "" : sectionDTO.getSectionName();
            }

            @Override
            public SectionDTO fromString(String s) {
                return null;
            }
        });

        sexDropdown.valueProperty().bindBidirectional(viewModel.getLearnerInformationVM().sexProperty());
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

        academicValidation.registerValidator(trackDropdown, Validator.createEmptyValidator("This field is required."));
        academicValidation.registerValidator(strandDropdown, Validator.createEmptyValidator("This field is required."));

        academicValidation.setErrorDecorationEnabled(false);
        academicValidation.initInitialDecoration();
    }

    private void setupControlButtons() {
        backBtn.setDisable(true);
        enrollmentPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() == 2) {
                nextBtn.setText("Save");
            } else {
                nextBtn.setText("Next");
            }

            backBtn.setDisable(!(newVal.intValue() > 0));
        }) ;
    }

    private void setupRadioButtons() {
        semErrorLabel.setVisible(false);
        semesterGroup.selectedToggleProperty().addListener((obs, oldV, newV) ->  {
            if (newV != null) {
                RadioButton selected = (RadioButton) newV;
                viewModel.getAcademicInformationVM().semesterProperty().set(Semester.fromString(selected.getText()));
                academicValidation.redecorate();
            }
        });

        viewModel.getAcademicInformationVM().semesterProperty().addListener((obs, oldSem, newSem) -> {
            if (newSem == null) {
                if (semesterGroup.getSelectedToggle() != null) {
                    semesterGroup.getSelectedToggle().setSelected(false);
                }
            } else if (newSem == Semester.FIRST) {
                firstRadio.setSelected(true);
            } else if (newSem == Semester.SECOND) {
                secondRadio.setSelected(true);
            }
        });
    }

    private void setupCheckboxes() {
        bindReq(beefCheck, 1);
        bindReq(sf9Check, 2);
        bindReq(psaCheck, 3);
        bindReq(goodMoralCheck, 4);
        bindReq(affidavitCheck, 5);
        bindReq(form5Check, 6);
        bindReq(alsCheck, 7);
    }

    private void bindReq(CheckBox cb, Integer key) {
        var reqVM = viewModel.getRequirementMap().get(key);
        if (reqVM != null) {
            cb.selectedProperty().bindBidirectional(reqVM.submittedProperty());

            reqVM.submittedProperty().addListener((obs, old, newVal) -> {
                System.out.println("Requirement " + key + " changed to: " + newVal);
            });
        } else {
            System.out.println("CRITICAL: Map key " + key + " was null during binding!");
        }
    }

    private void setupEditModeAutoSelect() {
        viewModel.getTracks().addListener((javafx.collections.ListChangeListener<TrackDTO>) c -> {
            if (viewModel.isEditing && !viewModel.getTracks().isEmpty()) {
                Gender savedGender = viewModel.getLearnerInformationVM().sexProperty().get();
                if (savedGender != null) {
                    sexDropdown.setValue(savedGender);
                }

                TrackDTO savedTrack = viewModel.getTracks().stream()
                        .filter(t -> t.getTrackId() == viewModel.getAcademicInformationVM().trackIdProperty().get())
                        .findFirst()
                        .orElse(null);

                if (savedTrack != null) {
                    trackDropdown.setValue(savedTrack);

                    Platform.runLater(() -> {
                        strandDropdown.getItems().stream()
                                .filter(s -> s.getStrandId() == viewModel.getAcademicInformationVM().strandIdProperty().get())
                                .findFirst()
                                .ifPresent(strand -> {
                                    strandDropdown.setValue(strand);

                                    Platform.runLater(() -> {
                                        sectionDropdown.getItems().stream()
                                                .filter(s -> s.getSectionId() == viewModel.getAcademicInformationVM().sectionIdProperty().get())
                                                .findFirst()
                                                .ifPresent(section -> sectionDropdown.setValue(section));
                                    });
                                });
                    });
                }
            }
        });
    }
}
