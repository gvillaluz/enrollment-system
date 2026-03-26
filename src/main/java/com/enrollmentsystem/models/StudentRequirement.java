package com.enrollmentsystem.models;

import java.time.LocalDate;

public class StudentRequirement {
    private int studentRequirementId;
    private String studentLrn;
    private int requirementReferenceId;
    private boolean is_submitted;
    private LocalDate dateSubmitted;

    public StudentRequirement() {}

    public StudentRequirement(int studentRequirementId, String studentLrn, int requirementReferenceId, boolean is_submitted, LocalDate dateSubmitted) {
        this.studentRequirementId = studentRequirementId;
        this.studentLrn = studentLrn;
        this.requirementReferenceId = requirementReferenceId;
        this.is_submitted = is_submitted;
        this.dateSubmitted = dateSubmitted;
    }

    public int getStudentRequirementId() { return studentRequirementId; }
    public void setStudentRequirementId(int studentRequirementId) { this.studentRequirementId = studentRequirementId; }

    public String getStudentLrn() { return studentLrn; }
    public void setStudentLrn(String studentLrn) { this.studentLrn = studentLrn; }

    public int getRequirementReferenceId() { return requirementReferenceId; }
    public void setRequirementReferenceId(int requirementReferenceId) { this.requirementReferenceId = requirementReferenceId; }

    public boolean isSubmitted() { return is_submitted; }
    public void setSubmitted(boolean is_submitted) { this.is_submitted = is_submitted; }

    public LocalDate getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(LocalDate dateSubmitted) { this.dateSubmitted = dateSubmitted; }
}
