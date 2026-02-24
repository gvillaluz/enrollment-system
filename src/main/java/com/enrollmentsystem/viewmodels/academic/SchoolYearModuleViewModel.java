package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.SchoolYearStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SchoolYearModuleViewModel {
    private final SchoolYearViewModel formInstance = new SchoolYearViewModel(new SchoolYearDTO());
    private final ObservableList<SchoolYearViewModel> schoolYears = FXCollections.observableArrayList();

    public SchoolYearViewModel getFormInstance() { return formInstance; }

    public ObservableList<SchoolYearViewModel> getSchoolYears() { return schoolYears; }

    public void loadSchoolYears() {
        schoolYears.addAll(
                new SchoolYearViewModel(new SchoolYearDTO(1, "2020", "2021", SchoolYearStatus.fromString("Open"))),
                new SchoolYearViewModel(new SchoolYearDTO(2, "2019", "2020", SchoolYearStatus.fromString("Closed"))),
                new SchoolYearViewModel(new SchoolYearDTO(3, "2018", "2019", SchoolYearStatus.fromString("Closed")))
        );
    }

    public void addSchoolYear() {
        String startYear = formInstance.startYearProperty().get();
        String endYear = formInstance.endYearProperty().get();
        String academicYear = formInstance.academicYearProperty().get();

        if (startYear.length() < 4 || endYear.length() < 4) {
            System.out.println("Invalid School Year: " + academicYear);
            return;
        }

        System.out.println(startYear);
        System.out.println(endYear);
        System.out.println(academicYear);

        int id = schoolYears.toArray().length + 1;
        SchoolYearDTO dto = new SchoolYearDTO(
                id,
                startYear,
                endYear,
                SchoolYearStatus.OPEN
        );

        schoolYears.add(new SchoolYearViewModel(dto));

        formInstance.startYearProperty().set("");
        formInstance.endYearProperty().set("");
    }

    public void openSchoolYear() {
        System.out.println("Open");
    }
}
