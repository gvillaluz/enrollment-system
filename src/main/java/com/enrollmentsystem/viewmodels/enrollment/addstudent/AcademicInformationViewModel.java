package com.enrollmentsystem.viewmodels.enrollment.addstudent;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.Semester;
import javafx.beans.property.*;

public class AcademicInformationViewModel {
    private final IntegerProperty academicInformationId = new SimpleIntegerProperty();
    private final ObjectProperty<Integer> lastGrLevel = new SimpleObjectProperty<>();
    private final StringProperty lastSchoolYear = new SimpleStringProperty();
    private final StringProperty lastSchoolAttended = new SimpleStringProperty();
    private final StringProperty lastSchoolId = new SimpleStringProperty();
    private final ObjectProperty<Semester> semester = new SimpleObjectProperty<>();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackName = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final StringProperty sectionName = new SimpleStringProperty();

    public IntegerProperty academicInformationIdProperty() { return academicInformationId; }
    public ObjectProperty<Integer> lastGrLevelProperty() { return lastGrLevel; }
    public StringProperty lastSchoolYearProperty() { return lastSchoolYear; }
    public StringProperty lastSchoolAttendedProperty() { return lastSchoolAttended; }
    public StringProperty lastSchoolIdProperty() { return lastSchoolId; }
    public ObjectProperty<Semester> semesterProperty() { return semester; }
    public StringProperty trackNameProperty() { return trackName; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }

    public IntegerProperty trackIdProperty() { return trackId; }
    public int getTrackId() { return trackId.get(); }
    public void setTrackId(int value) { this.trackId.set(value); }

    public IntegerProperty strandIdProperty() { return strandId; }
    public int getStrandId() { return strandId.get(); }
    public void setStrandId(int value) { this.strandId.set(value); }

    public boolean isSemEmpty() {
        return semester.get() == null;
    }

    public void updateFromDTO(EnrollmentFormDTO dto) {
        this.lastGrLevel.set(dto.getLastGradeLevel());
        this.lastSchoolYear.set(dto.getLastSchoolYear());
        this.lastSchoolAttended.set(dto.getLastSchoolName());
        this.lastSchoolId.set(dto.getLastSchoolId());
        this.semester.set(dto.getSemester());
        this.trackId.set(dto.getTrackId());
        this.trackName.set(dto.getTrackName());
        this.strandId.set(dto.getStrandId());
        this.strandCode.set(dto.getStrandName());
        this.sectionId.set(dto.getSectionId());
        this.sectionName.set(dto.getSectionName());
    }

    public void clear() {
        this.lastGrLevel.set(null);
        this.lastSchoolYear.set("");
        this.lastSchoolAttended.set("");
        this.lastSchoolId.set("");
        this.semester.set(null);
        this.trackName.set(null);
        this.strandCode.set(null);
        this.sectionId.set(0);
        this.sectionName.set(null);
    }
}
