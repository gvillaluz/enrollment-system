package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.StudentRequirementDTO;
import com.enrollmentsystem.models.StudentRequirement;
import javafx.beans.property.*;

import java.time.LocalDate;

public class StudentRequirementViewModel {
    private final IntegerProperty studentRequirementId = new SimpleIntegerProperty();
    private final StringProperty studentLrn = new SimpleStringProperty();
    private final IntegerProperty requirementRefId = new SimpleIntegerProperty();
    private final StringProperty requirementName = new SimpleStringProperty();
    private final BooleanProperty isSubmitted = new SimpleBooleanProperty();
    private final ObjectProperty<LocalDate> dateSubmitted = new SimpleObjectProperty<>();

    public StudentRequirementViewModel() {}

    public StudentRequirementViewModel(StudentRequirementDTO dto) {
        this.studentRequirementId.set(dto.getStudentRequirementId());
        this.studentLrn.set(dto.getStudentLrn());
        this.requirementRefId.set(dto.getRequirementRefId());
        this.requirementName.set(dto.getRequirementName());
        this.isSubmitted.set(dto.isSubmitted());
        this.dateSubmitted.set(dto.getDateSubmitted());
    }

    public IntegerProperty studentRequirementIdProperty() { return studentRequirementId; }
    public StringProperty studentLrnProperty() { return studentLrn; }
    public IntegerProperty requirementRefIdProperty() { return requirementRefId; }
    public StringProperty requirementNameProperty() { return requirementName; }
    public BooleanProperty submittedProperty() { return isSubmitted; }
    public ObjectProperty<LocalDate> dateSubmittedProperty() { return dateSubmitted; }
}
