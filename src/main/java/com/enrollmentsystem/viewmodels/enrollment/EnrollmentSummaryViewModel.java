package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EnrollmentSummaryViewModel {
    private final StringProperty lrn = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final StringProperty academicYear = new SimpleStringProperty();
    private final IntegerProperty grade = new SimpleIntegerProperty();
    private final StringProperty term = new SimpleStringProperty();
    private final StringProperty track = new SimpleStringProperty();
    private final StringProperty strand = new SimpleStringProperty();
    private final StringProperty section = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public EnrollmentSummaryViewModel(EnrollmentSummaryDTO dto) {
        this.lrn.set(dto.getLrn());
        this.lastName.set(dto.getLastName());
        this.firstName.set(dto.getFirstName());
        this.middleName.set(dto.getMiddleName());
        this.academicYear.set(dto.getAcademicYear());
        this.grade.set(dto.getGrade());
        this.term.set(dto.getTerm());
        this.track.set(dto.getTrack());
        this.strand.set(dto.getStrand());
        this.section.set(dto.getSection());
        this.status.set(dto.getStatus());
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
    public StringProperty statusProperty() { return status; }
}
