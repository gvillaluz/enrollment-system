package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.models.AuditLog;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.filters.AuditFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Integer countLogs(AuditFilter filter) {
        List<Object> params = new ArrayList<>();
        var query = new StringBuilder(
                "SELECT COUNT(au.log_id) FROM audit_log au " +
                "LEFT JOIN users u ON au.user_id = u.user_id WHERE 1=1 "
        );

        if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
            query.append("AND LOWER(u.username) LIKE LOWER(?) ");
            params.add("%" + filter.getUsername() + "%");
        }

        if (filter.getAction() != null) {
            query.append("AND au.action_type = ? ");
            params.add(filter.getAction().getValue());
        }

        if (filter.getModule() != null) {
            query.append("AND au.module_affected = ? ");
            params.add(filter.getModule().getValue());
        }

        if (filter.getDateFrom() != null) {
            query.append("AND au.timestamp >= ? ");
            params.add(filter.getDateFrom());
        }

        if (filter.getDateTo() != null) {
            query.append("AND au.timestamp <= ? ");
            params.add(filter.getDateTo());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            System.out.println("Failed to get total rows: " + e.getMessage());
            return null;
        }
    }

    public List<AuditLog> getLogs(AuditFilter filter) {
        List<AuditLog> logs = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        var query = new StringBuilder(
                "SELECT au.*, u.username " +
                "FROM audit_log au " +
                "LEFT JOIN users u ON au.user_id = u.user_id WHERE 1=1 "
        );

        if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
            query.append("AND LOWER(u.username) LIKE LOWER(?) ");
            params.add("%" + filter.getUsername() + "%");
        }

        if (filter.getAction() != null) {
            query.append("AND au.action_type = ? ");
            params.add(filter.getAction().getValue());
        }

        if (filter.getModule() != null) {
            query.append("AND au.module_affected = ? ");
            params.add(filter.getModule().getValue());
        }

        if (filter.getDateFrom() != null) {
            query.append("AND au.timestamp >= ? ");
            params.add(filter.getDateFrom());
        }

        if (filter.getDateTo() != null) {
            query.append("AND au.timestamp <= ? ");
            params.add(filter.getDateTo());
        }

        query.append("ORDER BY au.timestamp DESC LIMIT 15 OFFSET ?");
        params.add(filter.getOffset());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    var log = new AuditLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setUsername(rs.getString("username"));
                    log.setActionType(AuditAction.fromString(rs.getString("action_type")));
                    log.setModuleAffected(AuditModule.fromString(rs.getString("module_affected")));
                    log.setTargetKey(rs.getString("target_key"));
                    log.setDescription(rs.getString("description"));
                    log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());

                    logs.add(log);
                }
            }

            return logs;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to load audit trail logs: " + e.getMessage());
            return null;
        }
    }
}
