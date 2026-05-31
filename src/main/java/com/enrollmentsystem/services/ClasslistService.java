package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.models.SchoolYear;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.EnrollmentRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.SectionRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.ExcelHelper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClasslistService extends BaseService {
    private final EnrollmentRepository _repo;
    private final SectionRepository _secRepo;
    private final SchoolYearRepository _syRepo;

    public ClasslistService(EnrollmentRepository repo, SectionRepository secRepo, AuditRepository auditRepo, SchoolYearRepository syRepo) {
        super(auditRepo);
        _repo = repo;
        _secRepo = secRepo;
        _syRepo = syRepo;
    }

    public CompletableFuture<Boolean> checkSectionsExists() {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var schoolYearId = _syRepo.getActiveSchoolYearId(conn);

                return _secRepo.checkSectionsExists(conn, schoolYearId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException("Failed to check if section exists");
            }
        });
    }

    public CompletableFuture<String> getActiveSchoolYear() {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int syId = _syRepo.getActiveSchoolYearId(conn);
                SchoolYear schoolYear =  _syRepo.getSchoolYearById(conn, syId);

                return "S.Y. " + schoolYear.getStartYear() + "-" + schoolYear.getEndYear();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException("Failed to get active school year");
            }
        });
    }

    public CompletableFuture<List<ClasslistRecordDTO>> getClasslistRecords(int gradeLevel, int sectionId) {
        validateSession();

        if (gradeLevel <= 0 || sectionId <= 0)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Fields must not be empty."));

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _repo.getClasslistRecords(conn, gradeLevel, sectionId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException("Failed to get records");
            }
        });
    }

    public CompletableFuture<Boolean> exportRecord(List<ClasslistRecordDTO> classlist, File file, String sectionName, String schoolYear) {
        validateSession();

        if (classlist.isEmpty() || file == null)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid Record."));

        return CompletableFuture.supplyAsync(() -> {
            try {
                ExcelHelper.generate(classlist, file, sectionName, schoolYear);

                try (Connection conn = DatabaseConnection.getConnection()) {
                    logActivity(
                            conn,
                            file.getName(),
                            AuditAction.EXPORT,
                            AuditModule.CLASSLIST,
                            "Exported classlist record: " + file.getName()
                    );
                    return true;
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to write Excel file: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Audit Log Failed: " + e.getMessage());
                return true;
            }
        });
    }
}
