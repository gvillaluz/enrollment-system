package com.enrollmentsystem.mappers;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.models.AuditLog;

public class AuditLogBuilder {
    private AuditLog auditLog = new AuditLog();

    public AuditLogBuilder user(int userId) {
        auditLog.setUserId(userId);
        return this;
    }

    public AuditLogBuilder action(AuditAction action) {
        auditLog.setActionType(action);
        return this;
    }

    public AuditLogBuilder module(AuditModule module) {
        auditLog.setModuleAffected(module);
        return this;
    }

    public AuditLogBuilder target(String target) {
        auditLog.setTargetKey(target);
        return this;
    }

    public AuditLogBuilder description(String description) {
        auditLog.setDescription(description);
        return this;
    }

    public AuditLog build() {
        return auditLog;
    }
}
