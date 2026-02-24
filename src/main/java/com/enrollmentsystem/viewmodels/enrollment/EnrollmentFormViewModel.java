package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import javafx.beans.property.*;

import java.time.LocalDate;

public class EnrollmentFormViewModel {
    private final StringProperty lrn = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty middleName = new SimpleStringProperty("");
    private final StringProperty extensionName = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>();
    private final StringProperty age = new SimpleStringProperty();
    private final ObjectProperty<Gender> sex = new SimpleObjectProperty<>();

    private final IntegerProperty lastGrLevel = new SimpleIntegerProperty();
    private final StringProperty lastSchoolYear = new SimpleStringProperty();
    private final StringProperty lastSchoolAttended = new SimpleStringProperty();
    private final IntegerProperty lastSchoolId = new SimpleIntegerProperty();
    private final ObjectProperty<Semester> semester = new SimpleObjectProperty<>();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackName = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();

    public EnrollmentFormViewModel() {}

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty extensionNameProperty() { return extensionName; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public StringProperty ageProperty() { return age; }
    public ObjectProperty<Gender> sexProperty() { return sex; }

    public IntegerProperty lastGrLevelProperty() { return lastGrLevel; }
    public StringProperty lastSchoolYearProperty() { return lastSchoolYear; }
    public StringProperty lastSchoolAttendedProperty() { return lastSchoolAttended; }
    public IntegerProperty lastSchoolIdProperty() { return lastSchoolId; }
    public ObjectProperty<Semester> semesterProperty() { return semester; }
    public StringProperty trackNameProperty() { return trackName; }
    public StringProperty strandCodeProperty() { return strandCode; }

    public IntegerProperty trackIdProperty() { return trackId; }
    public int getTrackId() { return trackId.get(); }
    public void setTrackId(int value) { this.trackId.set(value); }

    public IntegerProperty strandIdProperty() { return strandId; }
    public int getStrandId() { return strandId.get(); }
    public void setStrandId(int value) { this.strandId.set(value); }

    public void clearLearnerInformationFields() {
        this.lrn.set("");
        this.lastName.set("");
        this.firstName.set("");
        this.middleName.set("");
        this.extensionName.set("");
        this.birthDate.set(null);
        this.age.set("");
        this.sex.set(null);
    }

    public void clearAcademicInformationFields() {
        this.lastGrLevel.set(0);
        this.lastSchoolYear.set("");
        this.lastSchoolAttended.set("");
        this.lastSchoolId.set(0);
        this.semester.set(null);
        this.trackName.set("");
        this.strandCode.set("");
    }

    public void clearRequirements() {}
}
