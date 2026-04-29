package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;

public class BatchRecordDTO {
    private int enrollmentId;
    private String lrn;
    private Gender gender;
    private int schoolYearId;
    private int gradeLevel;
    private Semester semester;
    private int trackId;
    private int strandId;
    private Integer sectionId;
    private EnrollmentStatus status;

    public BatchRecordDTO(int enrollmentId, String lrn, Gender gender, int schoolYearId, int gradeLevel,
                          Semester semester, int trackId, int strandId, Integer sectionId, EnrollmentStatus status) {
        this.enrollmentId = enrollmentId;
        this.lrn = lrn;
        this.gender = gender;
        this.schoolYearId = schoolYearId;
        this.gradeLevel = gradeLevel;
        this.semester = semester;
        this.trackId = trackId;
        this.strandId = strandId;
        this.sectionId = sectionId;
        this.status = status;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public Integer getSectionId() { return sectionId; }
    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
}
