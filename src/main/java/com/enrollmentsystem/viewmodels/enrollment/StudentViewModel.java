package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.models.Student;
import javafx.beans.property.*;

import java.time.LocalDate;

public class StudentViewModel {
    private final StringProperty lrn = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final StringProperty extensionName = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final ObjectProperty<Gender> sex = new SimpleObjectProperty<>();

    public StudentViewModel(Student model) {
        this.lrn.set(model.getLrn());
        this.lastName.set(model.getLastName());
        this.firstName.set(model.getFirstName());
        this.middleName.set(model.getMiddleName());
        this.extensionName.set(model.getExtensionName());
        this.birthDate.set(model.getBirthDate());
        this.age.set(model.getAge());
        this.sex.set(model.getSex());
    }

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty extensionNameProperty() { return extensionName; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public IntegerProperty ageProperty() { return age; }
    public ObjectProperty<Gender> sexProperty() { return sex; }
}
