package com.enrollmentsystem.models;

import com.enrollmentsystem.enums.Semester;

public class AcademicInformation {
    private int academicInformationId;
    private String studentLrn;
    private Integer lastGradeLevel;
    private String lastSchoolYear;
    private String lastSchoolName;
    private String lastSchoolId;

    public AcademicInformation() {}

    public AcademicInformation(int academicInformationId, String studentLrn, int lastGradeLevel, String lastSchoolYear, String lastSchoolName, String lastSchoolId) {
        this.academicInformationId = academicInformationId;
        this.studentLrn = studentLrn;
        this.lastGradeLevel = lastGradeLevel;
        this.lastSchoolYear = lastSchoolYear;
        this.lastSchoolName = lastSchoolName;
        this.lastSchoolId = lastSchoolId;
    }

    public int getAcademicInformationId() { return academicInformationId; }
    public void setAcademicInformationId(int academicInformationId) { this.academicInformationId = academicInformationId; }

    public String getStudentLrn() { return studentLrn; }
    public void setStudentLrn(String studentLrn) { this.studentLrn = studentLrn; }

    public Integer getLastGradeLevel() { return lastGradeLevel; }
    public void setLastGradeLevel(Integer lastGradeLevel) { this.lastGradeLevel = lastGradeLevel; }

    public String getLastSchoolYear() { return lastSchoolYear; }
    public void setLastSchoolYear(String lastSchoolYear) { this.lastSchoolYear = lastSchoolYear; }

    public String getLastSchoolName() { return lastSchoolName; }
    public void setLastSchoolName(String lastSchoolName) { this.lastSchoolName = lastSchoolName; }

    public String getLastSchoolId() { return lastSchoolId; }
    public void setLastSchoolId(String lastSchoolId) { this.lastSchoolId = lastSchoolId; }
}
