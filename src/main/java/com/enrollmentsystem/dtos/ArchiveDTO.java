package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.Semester;

public class ArchiveDTO {
    private int enrollmentId;
    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private int schoolYearId;
    private String schoolYear;
    private int gradeLevel;
    private Semester semester;
    private int trackId;
    private String trackCode;
    private int strandId;
    private String strandCode;

    public ArchiveDTO(int enrollmentId, String lrn, String lastName, String firstName, String middleName,
                      int schoolYearId, String schoolYear, int gradeLevel, Semester semester,
                      int trackId, String trackCode, int strandId, String strandCode) {
        this.enrollmentId = enrollmentId;
        this.lrn = lrn;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.schoolYearId = schoolYearId;
        this.schoolYear = schoolYear;
        this.gradeLevel = gradeLevel;
        this.semester = semester;
        this.trackId = trackId;
        this.trackCode = trackCode;
        this.strandId = strandId;
        this.strandCode = strandCode;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public String getStrandCode() { return strandCode; }
    public void setStrandCode(String strandCode) { this.strandCode = strandCode; }
}
