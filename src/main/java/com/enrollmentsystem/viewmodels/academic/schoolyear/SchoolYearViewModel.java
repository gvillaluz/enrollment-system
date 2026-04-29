package com.enrollmentsystem.viewmodels.academic.schoolyear;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.SchoolYearStatus;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class SchoolYearViewModel {
    private final IntegerProperty schoolYearId;
    private final IntegerProperty startYear;
    private final IntegerProperty endYear;
    private final ObjectProperty<SchoolYearStatus> status;
    private final StringProperty academicYear;

    public SchoolYearViewModel(SchoolYearDTO dto) {
        schoolYearId = new SimpleIntegerProperty(dto.getSchoolYearId());
        startYear = new SimpleIntegerProperty(dto.getStartYear());
        endYear = new SimpleIntegerProperty(dto.getEndYear());
        status = new SimpleObjectProperty<>(dto.getStatus());
        academicYear = new SimpleStringProperty();
        academicYear.bind(Bindings.concat("SY ", startYear, "-", endYear));
    }

    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }
    public IntegerProperty startYearProperty() { return startYear; }
    public IntegerProperty endYearProperty() { return endYear; }
    public ObjectProperty<SchoolYearStatus> statusProperty() { return status; }
    public StringProperty academicYearProperty() { return academicYear; }
}
