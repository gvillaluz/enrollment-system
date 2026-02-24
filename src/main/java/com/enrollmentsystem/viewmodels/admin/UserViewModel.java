package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.Role;
import javafx.beans.property.*;

public class UserViewModel {
    private final IntegerProperty userId;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty username;
    private final StringProperty password;
    private final ObjectProperty<Role> role;

    public UserViewModel(UserDTO dto) {
        this.userId = new SimpleIntegerProperty(dto.getUserId());
        this.lastName = new SimpleStringProperty(dto.getLastName());
        this.firstName = new SimpleStringProperty(dto.getFirstName());
        this.middleName = new SimpleStringProperty(dto.getMiddleName());
        this.username = new SimpleStringProperty(dto.getUsername());
        this.password = new SimpleStringProperty(dto.getPassword());
        this.role = new SimpleObjectProperty<>(dto.getRole());
    }

    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public ObjectProperty<Role> roleProperty() { return role; }
}