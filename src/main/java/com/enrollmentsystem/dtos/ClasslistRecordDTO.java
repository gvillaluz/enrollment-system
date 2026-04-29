package com.enrollmentsystem.dtos;

public class ClasslistRecordDTO {
    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private int gradeLevel;
    private int sectionId;
    private String sectionName;

    public ClasslistRecordDTO(String lrn, String lastName, String firstName, String middleName, int gradeLevel, int sectionId, String sectionName) {
        this.lrn = lrn;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gradeLevel = gradeLevel;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
}
