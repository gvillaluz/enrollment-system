package com.enrollmentsystem.viewmodels.academic.classlist;

import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import com.enrollmentsystem.enums.Gender;
import javafx.beans.property.*;

public class ClasslistRecordViewModel {
    private final StringProperty lrn;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final ObjectProperty<Gender> gender;
    private final IntegerProperty gradeLevel;
    private final IntegerProperty sectionId;
    private final StringProperty sectionName;

    public ClasslistRecordViewModel(ClasslistRecordDTO dto) {
        lrn = new SimpleStringProperty(dto.getLrn());
        lastName = new SimpleStringProperty(dto.getLastName());
        firstName = new SimpleStringProperty(dto.getFirstName());
        middleName = new SimpleStringProperty(dto.getMiddleName());
        gender = new SimpleObjectProperty<>(dto.getGender());
        gradeLevel = new SimpleIntegerProperty(dto.getGradeLevel());
        sectionId = new SimpleIntegerProperty(dto.getSectionId());
        sectionName = new SimpleStringProperty(dto.getSectionName());
    }

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public ObjectProperty<Gender> genderProperty() { return gender; }
    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }
}
