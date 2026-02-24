package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;

import java.time.LocalDateTime;

public class AuditDTO {
    private int logId;
    private int userId;
    private String username;
    private AuditAction action;
    private AuditModule module;
    private String description;
    private LocalDateTime timestamp;

    public AuditDTO(int logId, int userId, String username, AuditAction action, AuditModule module, String description, LocalDateTime timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.module = module;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public AuditModule getModule() { return module; }
    public void setModule(AuditModule module) { this.module = module; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
