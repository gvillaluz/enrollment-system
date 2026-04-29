package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.Role;

import java.time.LocalDateTime;

public class RequirementNoteDTO {
    private int requirementNoteId;
    private String note;
    private boolean isResolved;
    private String lrn;
    private int userId;
    private String username;
    private Role userRole;
    private LocalDateTime dateAdded;
    private LocalDateTime updatedAt;

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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Role getUserRole() { return userRole; }
    public void setUserRole(Role userRole) { this.userRole = userRole; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
