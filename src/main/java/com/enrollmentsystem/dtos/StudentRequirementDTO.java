package com.enrollmentsystem.dtos;

import java.time.LocalDate;

public class StudentRequirementDTO {
    private int studentRequirementId;
    private String studentLrn;
    private int requirementRefId;
    private String requirementName;
    private boolean isSubmitted;
    private LocalDate dateSubmitted;

    public StudentRequirementDTO(int studentRequirementId, String studentLrn, int requirementRefId, boolean isSubmitted, LocalDate dateSubmitted) {
        this.studentRequirementId = studentRequirementId;
        this.studentLrn = studentLrn;
        this.requirementRefId = requirementRefId;
        this.isSubmitted = isSubmitted;
        this.dateSubmitted = dateSubmitted;
    }

    public int getStudentRequirementId() { return studentRequirementId; }
    public void setStudentRequirementId(int studentRequirementId) { this.studentRequirementId = studentRequirementId; }

    public String getStudentLrn() { return studentLrn; }
    public void setStudentLrn(String studentLrn) { this.studentLrn = studentLrn; }

    public int getRequirementRefId() { return requirementRefId; }
    public void setRequirementRefId(int requirementRefId) { this.requirementRefId = requirementRefId; }

    public String getRequirementName() { return requirementName; }
    public void setRequirementName(String requirementName) { this.requirementName = requirementName; }

    public boolean isSubmitted() { return isSubmitted; }
    public void setSubmitted(boolean isSubmitted) { this.isSubmitted = isSubmitted; }

    public LocalDate getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(LocalDate dateSubmitted) { this.dateSubmitted = dateSubmitted; }
}
