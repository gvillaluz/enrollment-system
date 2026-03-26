package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.Gender;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.Period;

public class LearnerInformationViewModel {
    private final StringProperty lrn = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty middleName = new SimpleStringProperty("");
    private final StringProperty extensionName = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> age = new SimpleObjectProperty<>();
    private final ObjectProperty<Gender> sex = new SimpleObjectProperty<>();

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty extensionNameProperty() { return extensionName; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public ObjectProperty<Integer> ageProperty() { return age; }
    public ObjectProperty<Gender> sexProperty() { return sex; }

    public LearnerInformationViewModel() {
        birthDate.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                calculateAge(newVal);
            }
        });
    }

    public void updateFromDTO(EnrollmentFormDTO dto) {
        this.lrn.set(dto.getLrn());
        this.lastName.set(dto.getLastName());
        this.firstName.set(dto.getFirstName());
        this.middleName.set(dto.getMiddleName());
        this.extensionName.set(dto.getExtensionName());
        this.birthDate.set(dto.getBirthDate());
        this.age.set(dto.getAge());
        this.sex.set(dto.getSex());
    }

    public void clear() {
        this.lrn.set("");
        this.lastName.set("");
        this.firstName.set("");
        this.middleName.set("");
        this.extensionName.set("");
        this.birthDate.set(null);
        this.age.set(null);
        this.sex.set(null);
    }

    private void calculateAge(LocalDate date) {
        LocalDate now = LocalDate.now();
        if (date.isAfter(now)) {
            age.set(null);
        } else {
            int years = Period.between(date, now).getYears();
            age.set(years);
        }
    }
}
