package com.enrollmentsystem.models;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;

import java.time.LocalDateTime;

public class AuditLog {
    private int userId;
    private AuditAction actionType;
    private AuditModule moduleAffected;
    private String targetKey;
    private String description;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(int userId, AuditAction actionType, AuditModule moduleAffected, String targetKey, String description, LocalDateTime timestamp) {
        this.userId = userId;
        this.actionType = actionType;
        this.moduleAffected = moduleAffected;
        this.targetKey = targetKey;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public AuditAction getActionType() { return actionType; }
    public void setActionType(AuditAction actionType) { this.actionType = actionType; }

    public AuditModule getModuleAffected() { return moduleAffected; }
    public void setModuleAffected(AuditModule moduleAffected) { this.moduleAffected = moduleAffected; }

    public String getTargetKey() { return targetKey; }
    public void setTargetKey(String targetKey) { this.targetKey = targetKey; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
