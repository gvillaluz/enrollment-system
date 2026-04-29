package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.mappers.SchoolYearMapper;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SchoolYearService extends BaseService {
    private final SchoolYearRepository _syRepo;

    public SchoolYearService(SchoolYearRepository syRepo, AuditRepository auditRepo) {
        super(auditRepo);
        _syRepo = syRepo;
    }

    public CompletableFuture<List<SchoolYearDTO>> getAllSchoolYears() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _syRepo.getAllSchoolYears(conn).stream()
                        .map(SchoolYearMapper::toDTO)
                        .toList();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get school years.");
            }
        });
    }

    public CompletableFuture<Boolean> createSchoolYear(SchoolYearDTO dto) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        if (dto.getStartYear() < 1000 || dto.getStartYear() > 9999) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Start year must be exactly 4 digits."));
        }

        if (dto.getEndYear() != dto.getStartYear() + 1)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("End year must be exactly one year after start year.")
            );

        int currentYear = Year.now().getValue();

        if (dto.getStartYear() < currentYear || dto.getStartYear() > currentYear + 10)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid academic range.")
            );

        var sy = SchoolYearMapper.toNewModel(dto);
        String schoolYear = sy.getStartYear() + "-" + sy.getEndYear();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                if (_syRepo.existsByStartYear(conn, sy.getStartYear())) {
                    throw new IllegalArgumentException("School year already existing.");
                }

                try {
                    _syRepo.deactivateAllSchoolYears(conn);
                    _syRepo.addSchoolYear(conn, sy);
                    logActivity(
                            conn,
                            schoolYear,
                            AuditAction.ADD,
                            AuditModule.SCHOOL_YEAR_MODULE,
                            "Added new school year: " + schoolYear
                    );

                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to add school year.");
            }
        });
    }

    public CompletableFuture<Boolean> updateSchoolYear(SchoolYearDTO dto) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        String schoolYear = dto.getStartYear() + "-" + dto.getEndYear();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                if (_syRepo.existsByStartYear(conn, dto.getStartYear())) {
                    throw new IllegalArgumentException("Start year already existing.");
                }

                var existing = _syRepo.getSchoolYearById(conn, dto.getSchoolYearId());
                if (existing == null) throw new IllegalArgumentException("School year not found.");

                String existingAcademicYear = existing.getStartYear() + "-" + existing.getEndYear();

                existing.setStartYear(dto.getStartYear());
                existing.setEndYear(dto.getEndYear());

                try {
                    _syRepo.updateSchoolYear(conn, existing);
                    logActivity(
                            conn,
                            schoolYear,
                            AuditAction.UPDATE,
                            AuditModule.SCHOOL_YEAR_MODULE,
                            "Updated school year from " + existingAcademicYear + " to " + schoolYear
                    );

                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }

                return true;
            } catch (SQLException e) {
                System.out.println("Failed to update school year: " + e.getMessage());
                throw new RuntimeException("Failed to update school year.");
            }
        });
    }

    public CompletableFuture<Boolean> openSchoolYear(SchoolYearDTO dto) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                var schoolYear = _syRepo.getSchoolYearById(conn, dto.getSchoolYearId());
                if (schoolYear == null) throw new IllegalArgumentException("School year not found.");

                schoolYear.setStatus(SchoolYearStatus.OPEN);

                String schoolYearString = schoolYear.getStartYear() + "-" + schoolYear.getEndYear();

                try {
                    _syRepo.deactivateAllSchoolYears(conn);
                    _syRepo.openSchoolYearById(conn, schoolYear);
                    logActivity(
                            conn,
                            schoolYearString,
                            AuditAction.UPDATE,
                            AuditModule.SCHOOL_YEAR_MODULE,
                            "Manual Status Override: Set [" + schoolYearString + "] to 'Open' and deactivated all other active school years."
                    );

                    conn.commit();
                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to open school year.");
            }
        });
    }
}
