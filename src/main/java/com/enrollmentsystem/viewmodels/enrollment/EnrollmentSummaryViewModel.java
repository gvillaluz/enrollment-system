package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import javafx.beans.property.*;

public class EnrollmentSummaryViewModel {
    private final StringProperty lrn;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty academicYear;
    private final IntegerProperty grade;
    private final StringProperty term;
    private final StringProperty track;
    private final StringProperty strand;
    private final StringProperty section;
    private final ObjectProperty<EnrollmentStatus> status;

    public EnrollmentSummaryViewModel(EnrollmentSummaryDTO dto) {
        this.lrn = new SimpleStringProperty(dto.getLrn());
        this.lastName = new SimpleStringProperty(dto.getLastName());
        this.firstName = new SimpleStringProperty(dto.getFirstName());
        this.middleName = new SimpleStringProperty(dto.getMiddleName());
        this.academicYear = new SimpleStringProperty(dto.getAcademicYear());
        this.grade = new SimpleIntegerProperty(dto.getGrade());
        this.term = new SimpleStringProperty(dto.getTerm());
        this.track = new SimpleStringProperty(dto.getTrack());
        this.strand = new SimpleStringProperty(dto.getStrand());
        this.section = new SimpleStringProperty(dto.getSection());
        this.status = new SimpleObjectProperty<EnrollmentStatus>(dto.getStatus());
    }

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty academicYearProperty() { return academicYear; }
    public IntegerProperty gradeProperty() { return grade; }
    public StringProperty termProperty() { return term; }
    public StringProperty trackProperty() { return track; }
    public StringProperty strandProperty() { return strand; }
    public StringProperty sectionProperty() { return section; }
    public ObjectProperty<EnrollmentStatus> statusProperty() { return status; }
}
