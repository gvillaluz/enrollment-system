package com.enrollmentsystem.utils.filters;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;

public class StudentFilter {
    private String lrn;
    private String lastName;
    private String firstName;
    private Gender gender;
    private int gradeLevel;
    private Semester semester;
    private int trackId;
    private int strandId;
    private int sectionId;
    private int offset;

    public StudentFilter(String lrn, String lastName, String firstName, Gender gender, int gradeLevel, Semester semester, int trackId, int strandId, int sectionId, int offset) {
        this.lrn = lrn;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.gradeLevel = gradeLevel;
        this.semester = semester;
        this.trackId = trackId;
        this.strandId = strandId;
        this.sectionId = sectionId;
        this.offset = offset;
    }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }
}
