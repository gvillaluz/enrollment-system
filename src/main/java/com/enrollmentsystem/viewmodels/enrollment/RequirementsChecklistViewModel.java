package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class RequirementsChecklistViewModel {
    private final StringProperty searchValue = new SimpleStringProperty("");

    private final ObservableList<RequirementsSummaryViewModel> checklist = FXCollections.observableArrayList();

    public ObservableList<RequirementsSummaryViewModel> getChecklist() { return checklist; }

    public void loadSampleData() {
        checklist.clear();

        // Sample 1: All submitted
        checklist.add(new RequirementsSummaryViewModel(new RequirementSummaryDTO(
                "136682458531", "Lee", "Angelo", "Presentacion",
                true, true, true, true, true, true, true)));

        // Sample 2: Mixed submission (Partial)
        checklist.add(new RequirementsSummaryViewModel(new RequirementSummaryDTO(
                "136682458552", "Cruz", "Jazzlyn", "Garcia",
                true, false, true, true, false, false, false)));

        // Sample 3: Only basic forms
        checklist.add(new RequirementsSummaryViewModel(new RequirementSummaryDTO(
                "136542322431", "Decena", "Lei", "Reyes",
                true, false, false, false, false, true, false)));

        // Sample 4: No submissions yet
        checklist.add(new RequirementsSummaryViewModel(new RequirementSummaryDTO(
                "136123123125", "Salaysay", "Kian", "Bautista",
                false, false, false, false, false, false, false)));
    }

    public FilteredList<RequirementsSummaryViewModel> searchStudentByName() {
        FilteredList<RequirementsSummaryViewModel> filteredList = new FilteredList<>(checklist);

        filteredList.setPredicate(r -> {
            String searchText = searchValue.get().toLowerCase();
            String firstname = r.firstNameProperty().get().toLowerCase();
            String lastname = r.lastNameProperty().get().toLowerCase();

            return firstname.contains(searchText) || lastname.contains(searchText);
        });

        return filteredList;
    }

    public StringProperty searchValueProperty() { return searchValue; }
}
