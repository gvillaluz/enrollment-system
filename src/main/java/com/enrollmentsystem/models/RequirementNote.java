package com.enrollmentsystem.models;

import java.time.LocalDateTime;

public class RequirementNote {
    private int requirementNoteId;
    private String note;
    private boolean isResolved;
    private String lrn;
    private int userId;
    private LocalDateTime dateAdded;
    private LocalDateTime updatedAt;

    public RequirementNote() {}

    public RequirementNote(int requirementNoteId, String note, boolean isResolved, String lrn, int userId, LocalDateTime dateAdded, LocalDateTime updatedAt) {
        this.requirementNoteId = requirementNoteId;
        this.note = note;
        this.isResolved = isResolved;
        this.lrn = lrn;
        this.userId = userId;
        this.dateAdded = dateAdded;
        this.updatedAt = updatedAt;
    }

    public int getRequirementNoteId() { return requirementNoteId; }
    public void setRequirementNoteId(int requirementNoteId) { this.requirementNoteId = requirementNoteId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isResolved() { return isResolved; }
    public void setResolved(boolean isResolved) { this.isResolved = isResolved; }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
