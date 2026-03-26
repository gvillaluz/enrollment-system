package com.enrollmentsystem.models;

import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Semester;

import java.time.LocalDate;

public class Enrollment {
    private int enrollmentId;
    private String studentLrn;
    private Integer sectionId;
    private int schoolYearId;
    private Semester semTerm;
    private int trackId;
    private int strandId;
    private int gradeLevel;
    private EnrollmentStatus enrollmentStatus;
    private LocalDate dateEnrolled;
    private int userId;

    public Enrollment() {}

    public Enrollment(int enrollmentId, String studentLrn, Integer sectionId, int schoolYearId, Semester semTerm, int trackId, int strandId, int gradeLevel, EnrollmentStatus enrollmentStatus, LocalDate dateEnrolled, int userId) {
        this.enrollmentId = enrollmentId;
        this.studentLrn = studentLrn;
        this.sectionId = sectionId;
        this.schoolYearId = schoolYearId;
        this.semTerm = semTerm;
        this.trackId = trackId;
        this.strandId = strandId;
        this.gradeLevel = gradeLevel;
        this.enrollmentStatus = enrollmentStatus;
        this.dateEnrolled = dateEnrolled;
        this.userId = userId;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getStudentLrn() { return studentLrn; }
    public void setStudentLrn(String studentLrn) { this.studentLrn = studentLrn; }

    public Integer getSectionId() { return sectionId; }
    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public Semester getSemTerm() { return semTerm; }
    public void setSemTerm(Semester semTerm) { this.semTerm = semTerm; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public EnrollmentStatus getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public LocalDate getDateEnrolled() { return dateEnrolled; }
    public void setDateEnrolled(LocalDate dateEnrolled) { this.dateEnrolled = dateEnrolled; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
