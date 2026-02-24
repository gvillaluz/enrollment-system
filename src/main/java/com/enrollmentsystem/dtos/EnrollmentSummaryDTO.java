package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.EnrollmentStatus;

import java.time.LocalDate;

public class EnrollmentSummaryDTO {
    private String LRN;
    private String lastName;
    private String firstName;
    private String middleName;
    private String academicYear;
    private Integer grade;
    private String term;
    private String track;
    private String strand;
    private String section;
    private EnrollmentStatus status;

    public EnrollmentSummaryDTO(String LRN, String lastName, String firstName, String middleName,
                                String academicYear, Integer grade, String term, String track,
                                String strand, String section, EnrollmentStatus status) {
        this.LRN = LRN;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.academicYear = academicYear;
        this.grade = grade;
        this.term = term;
        this.track = track;
        this.strand = strand;
        this.section = section;
        this.status = status;
    }

    public String getLrn() { return LRN; }
    public void setLrn(String lrn) { this.LRN = LRN; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }

    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
}
