package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.AuditDTO;
import com.enrollmentsystem.mappers.AuditMapper;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.filters.AuditFilter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuditService {
    private final AuditRepository _repo;

    public AuditService(AuditRepository repo) {
        _repo = repo;
    }

    public CompletableFuture<Integer> getTotalCount(AuditFilter filter) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var totalRows = _repo.countLogs(conn, filter);

                if (totalRows == null) {
                    throw new IllegalArgumentException("Failed to get total rows");
                }

                return totalRows;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get row count.");
            }
        });
    }

    public CompletableFuture<List<AuditDTO>> getAllLogs(AuditFilter filter) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
           try (Connection conn = DatabaseConnection.getConnection()) {
               var logs = _repo.getLogs(conn, filter);

               if (logs == null)
                   throw new IllegalArgumentException("Failed to load audit logs");


               return logs.stream()
                       .map(AuditMapper::toDTO)
                       .toList();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               throw new RuntimeException("Failed to load audit logs.");
           }
        });
    }
}
