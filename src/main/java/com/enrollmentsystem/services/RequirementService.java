package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.models.StudentRequirement;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.RequirementRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.ValidationHelper;
import com.enrollmentsystem.viewmodels.enrollment.RequirementsSummaryViewModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RequirementService extends BaseService {
    private final RequirementRepository _requirementRepo;
    private final SchoolYearRepository _syRepo;

    public RequirementService(RequirementRepository repo, SchoolYearRepository syRepo, AuditRepository auditRepo) {
        _requirementRepo = repo;
        _syRepo = syRepo;
        super(auditRepo);
    }

    public CompletableFuture<Integer> getRowCount(String searchValue) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            int schoolYearId = _syRepo.getActiveSchoolYearId();

            if (schoolYearId <= 0) {
                System.out.println("No active school year id");
                throw new IllegalArgumentException("Failed to load checklist");
            }

            return _requirementRepo.getRequirementRowCount(schoolYearId, searchValue);
        });
    }

    public CompletableFuture<List<RequirementSummaryDTO>> getRequirementsChecklist(String searchValue, int offset) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            int schoolYearId = _syRepo.getActiveSchoolYearId();

            if (schoolYearId <= 0) {
                System.out.println("No active school year id");
                throw new IllegalArgumentException("Failed to load checklist");
            }

            return _requirementRepo.getAllChecklist(schoolYearId, searchValue, offset);
        });
    }

    public CompletableFuture<Void> updateStudentRequirement(String lrn, int refId, boolean isSubmitted) {
        validateSession();

        if (ValidationHelper.isNullOrEmpty(lrn) || refId == 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid requirement.")
            );

        return CompletableFuture.supplyAsync(() -> {
            if (_requirementRepo.updateRequirement(lrn, refId, isSubmitted)) {
                System.out.println("Success");
                logActivity(lrn, AuditAction.UPDATE, AuditModule.REQUIREMENTS, "Updated student requirement: " + lrn);
            }
            return null;
        });
    }
}
