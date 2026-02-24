package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.services.EnrollmentService;
import com.enrollmentsystem.services.StudentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;
import java.util.logging.Filter;

public class EnrollmentViewModel {
    private final StringProperty searchValue = new SimpleStringProperty();

    private final EnrollmentService _service = AppContext.getEnrollmentService();
    private final ObservableList<EnrollmentSummaryViewModel> studentList = FXCollections.observableArrayList();
    ObservableList<EnrollmentSummaryViewModel> dummyList = FXCollections.observableArrayList();

    public FilteredList<EnrollmentSummaryViewModel> getEnrollmentList(int gradeLevel) {
        FilteredList<EnrollmentSummaryViewModel> filteredList = new FilteredList<>(dummyList);

        filteredList.setPredicate(item -> item.gradeProperty().get() == gradeLevel);
        return filteredList;
    }

    public void loadData() {
        var dummy = new EnrollmentSummaryDTO("109823746512", "Santos", "Einstein", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", EnrollmentStatus.ENROLLED);

        var dummy2 = new EnrollmentSummaryDTO("109823746512", "Salaysay", "James", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", EnrollmentStatus.ENROLLED);

        var dummy3 = new EnrollmentSummaryDTO("109823746512", "Mier", "Maria", "Reyes",
                "2025-2026", 12, "1st", "Academic", "STEM", "Einstein", EnrollmentStatus.ENROLLED);

        var dummy4 = new EnrollmentSummaryDTO("109823746512", "Lee", "Maria Clara", "Reyes",
                "2025-2026", 12, "1st", "Academic", "STEM", "Einstein", EnrollmentStatus.ENROLLED);

        var newDummy = new EnrollmentSummaryViewModel(dummy);
        var newDummy2 = new EnrollmentSummaryViewModel(dummy2);
        var newDummy3 = new EnrollmentSummaryViewModel(dummy3);
        var newDummy4 = new EnrollmentSummaryViewModel(dummy4);
        dummyList.add(newDummy);
        dummyList.add(newDummy2);
        dummyList.add(newDummy3);
        dummyList.add(newDummy4);
    }

    public FilteredList<EnrollmentSummaryViewModel> searchStudentByName(int gradeLevel) {
        FilteredList<EnrollmentSummaryViewModel> filteredList = new FilteredList<>(dummyList);

        filteredList.setPredicate(student -> {
            String searchText = searchValue.get().toLowerCase();
            String firstname = student.firstNameProperty().get().toLowerCase();
            String lastname = student.lastNameProperty().get().toLowerCase();
            int studentGradeLevel = student.gradeProperty().get();

            return (firstname.contains(searchText) || lastname.contains(searchText)) && gradeLevel == studentGradeLevel;
        });

        return filteredList;
    }

    public StringProperty searchValueProperty() { return searchValue; }
}
