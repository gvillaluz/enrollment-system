package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.SectionDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SectionViewModel {
    private final IntegerProperty sectionId;
    private final StringProperty sectionName;
    private final IntegerProperty maxCapacity;
    private final IntegerProperty schoolYearId;
    private final StringProperty schoolYear;
    private final IntegerProperty strandId;
    private final StringProperty strandCode;
    private final StringProperty roomAssignment;

    public SectionViewModel(SectionDTO dto) {
        this.sectionId = new SimpleIntegerProperty(dto.getSectionId());
        this.sectionName = new SimpleStringProperty(dto.getSectionName());
        this.maxCapacity = new SimpleIntegerProperty(dto.getMaxCapacity());
        this.schoolYearId = new SimpleIntegerProperty(dto.getSchoolYearId());
        this.schoolYear = new SimpleStringProperty(dto.getSchoolYear());
        this.strandId = new SimpleIntegerProperty(dto.getStrandId());
        this.strandCode = new SimpleStringProperty(dto.getStrandCode());
        this.roomAssignment = new SimpleStringProperty(dto.getRoomAssignment());
    }

    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }
    public IntegerProperty maxCapacityProperty() { return maxCapacity; }
    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }
    public StringProperty schoolYearProperty() { return schoolYear; }
    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public StringProperty roomAssignmentProperty() { return roomAssignment; }
}
