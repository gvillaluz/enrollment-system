package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.EnrollmentDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Semester;
import javafx.beans.property.*;

public class EnrollmentSummaryViewModel {
    private final IntegerProperty enrollmentId;
    private final StringProperty lrn;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty academicYear;
    private final IntegerProperty grade;
    private final ObjectProperty<Semester> term;
    private final IntegerProperty trackId;
    private final StringProperty trackCode;
    private final IntegerProperty strandId;
    private final StringProperty strandCode;
    private final IntegerProperty sectionId;
    private final StringProperty sectionName;
    private final ObjectProperty<EnrollmentStatus> status;

    public EnrollmentSummaryViewModel(EnrollmentDTO dto) {
        this.enrollmentId = new SimpleIntegerProperty(dto.getEnrollmentId());
        this.lrn = new SimpleStringProperty(dto.getLrn());
        this.lastName = new SimpleStringProperty(dto.getLastName());
        this.firstName = new SimpleStringProperty(dto.getFirstName());
        this.middleName = new SimpleStringProperty(dto.getMiddleName());
        this.academicYear = new SimpleStringProperty(dto.getSchoolYear());
        this.grade = new SimpleIntegerProperty(dto.getGradeLevel());
        this.term = new SimpleObjectProperty<>(dto.getSemester());
        this.trackId = new SimpleIntegerProperty(dto.getTrackId());
        this.trackCode = new SimpleStringProperty(dto.getTrackCode());
        this.strandId = new SimpleIntegerProperty(dto.getStrandId());
        this.strandCode = new SimpleStringProperty(dto.getStrandCode());
        this.sectionId = new SimpleIntegerProperty(dto.getSectionId());
        this.sectionName = new SimpleStringProperty(dto.getSectionName());
        this.status = new SimpleObjectProperty<EnrollmentStatus>(dto.getStatus());
    }

    public IntegerProperty enrollmentIdProperty() { return enrollmentId; }
    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty academicYearProperty() { return academicYear; }
    public IntegerProperty gradeProperty() { return grade; }
    public ObjectProperty<Semester> termProperty() { return term; }
    public StringProperty trackProperty() { return trackCode; }
    public StringProperty strandProperty() { return strandCode; }
    public StringProperty sectionProperty() { return sectionName; }
    public ObjectProperty<EnrollmentStatus> statusProperty() { return status; }
}
