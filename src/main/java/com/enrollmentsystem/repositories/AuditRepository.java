package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditRepository {
    public void log(Connection conn, AuditLog log) throws SQLException {
        String query = "INSERT INTO audit_log (user_id, action_type, module_affected, target_key, description) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, log.getUserId());
            statement.setString(2, log.getActionType().getValue());
            statement.setString(3, log.getModuleAffected().getValue());
            statement.setString(4, log.getTargetKey());
            statement.setString(5, log.getDescription());

            statement.executeUpdate();
        }
    }
}
