package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.SchoolYearStatus;

public class SchoolYearDTO {
    private int schoolYearId;
    private int startYear;
    private int endYear;
    private SchoolYearStatus status;

    public SchoolYearDTO() {}

    public SchoolYearDTO(int schoolYearId, int startYear, int endYear, SchoolYearStatus status) {
        this.schoolYearId = schoolYearId;
        this.startYear = startYear;
        this.endYear = endYear;
        this.status = status;
    }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public int getStartYear() { return startYear; }
    public void setStartYear(int startYear) { this.startYear = startYear; }

    public int getEndYear() { return endYear; }
    public void setEndYear(int endYear) { this.endYear = endYear; }

    public SchoolYearStatus getStatus() { return status; }
    public void setStatus(SchoolYearStatus status) { this.status = status; }
}
