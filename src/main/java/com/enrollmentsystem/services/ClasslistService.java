package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.EnrollmentRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.SectionRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

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

    public CompletableFuture<List<ClasslistRecordDTO>> getClasslistRecords(int gradeLevel, int sectionId) {
        validateSession();

        if (gradeLevel <= 0 || sectionId <= 0)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Fields must not be empty."));

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _repo.getClasslistRecords(conn, gradeLevel, sectionId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new IllegalArgumentException("Failed to get records");
            }
        });
    }
}
