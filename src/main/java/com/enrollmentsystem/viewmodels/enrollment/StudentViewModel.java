package com.enrollmentsystem.viewmodels.enrollment;

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
    private final StringProperty sex = new SimpleStringProperty();
    private final StringProperty placeOfBirth = new SimpleStringProperty();
    private final StringProperty motherTongue = new SimpleStringProperty();
    private final BooleanProperty isIpMember = new SimpleBooleanProperty();
    private final StringProperty ipCommunityName = new SimpleStringProperty();
    private final BooleanProperty is4PsBeneficiary = new SimpleBooleanProperty();
    private final StringProperty householdId4Ps = new SimpleStringProperty();
    private final BooleanProperty hasDisability = new SimpleBooleanProperty();

    public StudentViewModel(Student model) {
        this.lrn.set(model.getLrn());
        this.lastName.set(model.getLastName());
        this.firstName.set(model.getFirstName());
        this.middleName.set(model.getMiddleName());
        this.extensionName.set(model.getExtensionName());
        this.birthDate.set(model.getBirthDate());
        this.age.set(model.getAge());
        this.sex.set(model.getSex());
        this.placeOfBirth.set(model.getPlaceOfBirth());
        this.motherTongue.set(model.getMotherTongue());
        this.isIpMember.set(model.getIsIpMember());
        this.ipCommunityName.set(model.getIpCommunityName());
        this.is4PsBeneficiary.set(model.getIs4PsBeneficiary());
        this.householdId4Ps.set(model.getHouseholdId4Ps());
        this.hasDisability.set(model.getHasDisability());
    }

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty extensionNameProperty() { return extensionName; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty sexProperty() { return sex; }
    public StringProperty placeOfBirthProperty() { return placeOfBirth; }
    public StringProperty motherTongueProperty() { return motherTongue; }
    public BooleanProperty isIpMemberProperty() { return isIpMember; }
    public StringProperty ipCommunityNameProperty() { return ipCommunityName; }
    public BooleanProperty is4PsBeneficiaryProperty() { return is4PsBeneficiary; }
    public StringProperty householdId4PsProperty() { return householdId4Ps; }
    public BooleanProperty hasDisabilityProperty() { return hasDisability; }
}
