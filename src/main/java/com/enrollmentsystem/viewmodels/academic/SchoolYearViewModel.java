package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.SchoolYearStatus;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class SchoolYearViewModel {
    private final IntegerProperty schoolYearId;
    private final StringProperty startYear;
    private final StringProperty endYear;
    private final ObjectProperty<SchoolYearStatus> status;
    private final StringProperty academicYear;

    public SchoolYearViewModel(SchoolYearDTO dto) {
        schoolYearId = new SimpleIntegerProperty(dto.getSchoolYearId());
        startYear = new SimpleStringProperty(dto.getStartYear());
        endYear = new SimpleStringProperty(dto.getEndYear());
        status = new SimpleObjectProperty<>(dto.getStatus());
        academicYear = new SimpleStringProperty();
        academicYear.bind(Bindings.concat(startYear, "-", endYear));
    }

    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }
    public StringProperty startYearProperty() { return startYear; }
    public StringProperty endYearProperty() { return endYear; }
    public ObjectProperty<SchoolYearStatus> statusProperty() { return status; }
    public StringProperty academicYearProperty() { return academicYear; }
}
