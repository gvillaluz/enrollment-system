package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.ArchiveDTO;
import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.SchoolYearMapper;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.ArchiveRepository;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.filters.ArchiveFilter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArchiveService extends BaseService {
    private final ArchiveRepository _archiveRepo;
    private final SchoolYearRepository _schoolYearRepo;

    public ArchiveService(ArchiveRepository archiveRepo, AuditRepository auditRepo, SchoolYearRepository schoolYearRepo) {
        super(auditRepo);
        _archiveRepo = archiveRepo;
        _schoolYearRepo = schoolYearRepo;
    }

    public CompletableFuture<Integer> getRowCount(ArchiveFilter filter) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _archiveRepo.getTotalRows(conn, filter);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get row count");
            }
        });
    }

    public CompletableFuture<List<SchoolYearDTO>> getSchoolYears() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _schoolYearRepo.getAllSchoolYears(conn).stream()
                        .map(SchoolYearMapper::toDTO).toList();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load school years.");
            }
        });
    }

    public CompletableFuture<List<ArchiveDTO>> getArchiveRecords(ArchiveFilter filter) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _archiveRepo.getArchiveEnrollments(conn, filter);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load archive records.");
            }
        });
    }

    public CompletableFuture<Boolean> restoreRecord(int archiveId, String lrn) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        if (archiveId == 0 || lrn.isBlank())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid record.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                _archiveRepo.updateRecordStatus(conn, archiveId);

                logActivity(
                        conn,
                        "LRN: " + lrn,
                        AuditAction.UPDATE,
                        AuditModule.ARCHIVE_RECORDS,
                        "Restored enrollment record: " + lrn
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> deleteRecord(int archiveId, String lrn) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        if (archiveId == 0 || lrn.isBlank())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid record")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                _archiveRepo.deleteRecord(conn, archiveId);

                logActivity(
                        conn,
                        "LRN: " + lrn,
                        AuditAction.DELETE,
                        AuditModule.ARCHIVE_RECORDS,
                        "Deleted enrollment record: " + lrn
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
    }
}
