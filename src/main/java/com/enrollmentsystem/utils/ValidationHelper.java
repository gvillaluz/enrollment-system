package com.enrollmentsystem.utils;

import com.enrollmentsystem.viewmodels.enrollment.AcademicInformationViewModel;
import com.enrollmentsystem.viewmodels.enrollment.LearnerInformationViewModel;

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

    private static boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}
