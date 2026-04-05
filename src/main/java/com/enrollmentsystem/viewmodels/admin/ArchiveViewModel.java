package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.ArchiveDTO;
import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.services.ArchiveService;
import com.enrollmentsystem.utils.filters.ArchiveFilter;
import com.enrollmentsystem.viewmodels.enrollment.EnrollmentSummaryViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArchiveViewModel {
    private final IntegerProperty enrollmentId = new SimpleIntegerProperty();
    private final StringProperty lrn = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final IntegerProperty schoolYearId = new SimpleIntegerProperty();
    private final StringProperty schoolYear = new SimpleStringProperty();
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final ObjectProperty<Semester> semester = new SimpleObjectProperty<>();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackCode = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();

    public ArchiveViewModel(ArchiveDTO dto) {
        enrollmentId.set(dto.getEnrollmentId());
        lrn.set(dto.getLrn());
        lastName.set(dto.getLastName());
        firstName.set(dto.getFirstName());
        middleName.set(dto.getMiddleName());
        schoolYearId.set(dto.getSchoolYearId());
        schoolYear.set(dto.getSchoolYear());
        gradeLevel.set(dto.getGradeLevel());
        semester.set(dto.getSemester());
        trackId.set(dto.getTrackId());
        trackCode.set(dto.getTrackCode());
        strandId.set(dto.getStrandId());
        strandCode.set(dto.getStrandCode());
    }

    public IntegerProperty enrollmentIdProperty() { return enrollmentId; }
    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }
    public StringProperty schoolYearProperty() { return schoolYear; }
    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public ObjectProperty<Semester> semesterProperty() { return semester; }
    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
}
