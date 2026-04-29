package com.enrollmentsystem.viewmodels.enrollment.student;

import com.enrollmentsystem.dtos.StudentRecordDTO;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import javafx.beans.property.*;

public class RecordViewModel {
    private final IntegerProperty enrollmentId = new SimpleIntegerProperty();
    private final StringProperty lrn = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>();
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final ObjectProperty<Semester> semester = new SimpleObjectProperty<>();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackCode = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final StringProperty sectionName = new SimpleStringProperty();

    public RecordViewModel(StudentRecordDTO dto) {
        this.enrollmentId.set(dto.getEnrollmentId());
        this.lrn.set(dto.getLrn());
        this.lastName.set(dto.getLastName());
        this.firstName.set(dto.getFirstName());
        this.middleName.set(dto.getMiddleName());
        this.gender.set(dto.getGender());
        this.gradeLevel.set(dto.getGradeLevel());
        this.semester.set(dto.getSemester());
        this.trackId.set(dto.getTrackId());
        this.trackCode.set(dto.getTrackCode());
        this.strandId.set(dto.getStrandId());
        this.strandCode.set(dto.getStrandCode());
        this.sectionId.set(dto.getSectionId());
        this.sectionName.set(dto.getSectionName());
    }

    public IntegerProperty enrollmentIdProperty() { return enrollmentId; }
    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public ObjectProperty<Gender> genderProperty() { return gender; }
    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public ObjectProperty<Semester> semesterProperty() { return semester; }
    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }
}
