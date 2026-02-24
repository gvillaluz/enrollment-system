package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.SchoolYearStatus;

public class SchoolYearDTO {
    private int schoolYearId;
    private String startYear;
    private String endYear;
    private SchoolYearStatus status;

    public SchoolYearDTO() {
        startYear = "";
        endYear = "";
    }

    public SchoolYearDTO(int schoolYearId, String startYear, String endYear, SchoolYearStatus status) {
        this.schoolYearId = schoolYearId;
        this.startYear = startYear;
        this.endYear = endYear;
        this.status = status;
    }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getStartYear() { return startYear; }
    public void setStartYear(String startYear) { this.startYear = startYear; }

    public String getEndYear() { return endYear; }
    public void setEndYear(String endYear) { this.endYear = endYear; }

    public SchoolYearStatus getStatus() { return status; }
    public void setStatus(SchoolYearStatus status) { this.status = status; }
}
