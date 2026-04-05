package com.enrollmentsystem.utils;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.viewmodels.admin.UserFormViewModel;
import com.enrollmentsystem.viewmodels.admin.UserViewModel;
import com.enrollmentsystem.viewmodels.enrollment.AcademicInformationViewModel;
import com.enrollmentsystem.viewmodels.enrollment.LearnerInformationViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

public class ValidationHelper {
    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isBlank();
    }

    public static boolean isValid(
            LearnerInformationViewModel learner,
            AcademicInformationViewModel academic) {

        // Check Learner Info
        if (isInvalid(learner.lrnProperty().get()) || !learner.lrnProperty().get().matches("\\d{12}")) return false;
        if (isInvalid(learner.lastNameProperty().get())) return false;
        if (isInvalid(learner.firstNameProperty().get())) return false;
        if (learner.birthDateProperty().get() == null) return false;
        if (learner.ageProperty().get() <= 0) return false;
        if (learner.sexProperty().get() == null) return false;

        // Check Academic Info
        if (academic.semesterProperty().get() == null) return false;
        if (academic.getTrackId() <= 0) return false;
        if (academic.getStrandId() <= 0) return false;

        return true; // Everything passed
    }

    public static void validateUserFields(
            StringProperty firstName,
            StringProperty lastName,
            StringProperty username,
            Property<Role> role) throws IllegalArgumentException {

        // 1. Check for Nulls or Empty Strings
        if (isInvalid(firstName.get())) throw new IllegalArgumentException("First Name is required.");
        if (isInvalid(lastName.get())) throw new IllegalArgumentException("Last Name is required.");
        if (isInvalid(username.get())) throw new IllegalArgumentException("Username is required.");

        // 2. Role Validation
        if (role.getValue() == null) throw new IllegalArgumentException("User Role must be selected.");

        // 3. Username Complexity (Example: Min 4 characters)
        if (username.get().length() < 4) {
            throw new IllegalArgumentException("Username must be at least 4 characters long.");
        }
    }

    public static boolean hasChanges(UserFormViewModel formViewModel, UserViewModel user) {
        return user.lastNameProperty().get().equals(formViewModel.lastNameProperty().get()) &&
                user.firstNameProperty().get().equals(formViewModel.firstNameProperty().get()) &&
                user.middleNameProperty().get().equals(formViewModel.middleNameProperty().get()) &&
                user.usernameProperty().get().equals(formViewModel.usernameProperty().get()) &&
                user.roleProperty().get().equals(formViewModel.roleProperty().get());
    }

    private static boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}
