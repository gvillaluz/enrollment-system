package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.ArchiveDTO;
import com.enrollmentsystem.dtos.EnrollmentDTO;
import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.mappers.SchoolYearMapper;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.ArchiveRepository;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.filters.ArchiveFilter;
import com.enrollmentsystem.utils.filters.AuditFilter;

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

        return CompletableFuture.supplyAsync(() -> _archiveRepo.getTotalRows(filter));
    }

    public CompletableFuture<List<SchoolYearDTO>> getSchoolYears() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access")
            );

        return CompletableFuture.supplyAsync(() -> _schoolYearRepo.getAllSchoolYears().stream()
                .map(SchoolYearMapper::toDTO).toList());
    }

    public CompletableFuture<List<ArchiveDTO>> getArchiveRecords(ArchiveFilter filter) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> _archiveRepo.getArchiveEnrollments(filter));
    }
}
