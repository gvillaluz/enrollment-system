package com.enrollmentsystem.services;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.AuditLogBuilder;
import com.enrollmentsystem.models.AuditLog;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseService {
    protected final AuditRepository _auditRepo;

    protected BaseService(AuditRepository auditRepo) {
        _auditRepo = auditRepo;
    }

    protected  void logActivity(Connection conn, int userId, String targetKey, AuditAction action, AuditModule module, String description) {
        try {
            AuditLog audit = new AuditLogBuilder()
                    .user(userId)
                    .action(action)
                    .module(module)
                    .target(targetKey)
                    .description(description)
                    .build();

            _auditRepo.log(conn, audit);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to save log : " + e.getMessage());
        }
    }

    protected void logActivity(Connection conn, String targetKey, AuditAction action, AuditModule module, String description) {
        logActivity(conn, UserSession.getInstance().getUser().getUserId(), targetKey, action, module, description);
    }

    protected void logActivity(String targetKey, AuditAction action, AuditModule module, String description) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            logActivity(conn, UserSession.getInstance().getUser().getUserId(), targetKey, action, module, description);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void logActivity(int userId, String targetKey, AuditAction action, AuditModule module, String description) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            logActivity(conn, userId, targetKey, action, module, description);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void validateSession() {
        if (UserSession.getInstance() == null) {
            throw new SecurityException("Unauthorized access. Please log in.");
        }
    }
}
