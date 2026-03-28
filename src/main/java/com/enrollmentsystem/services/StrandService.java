package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.StrandMapper;
import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.StrandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StrandService extends BaseService {
    private final StrandRepository _repo;

    public StrandService(StrandRepository repository, AuditRepository auditRepo) {
        super(auditRepo);
        this._repo = repository;
    }

    public CompletableFuture<List<StrandDTO>> loadStrands() {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            var strands = _repo.getAllStrands();

            if (strands == null) {
                throw new IllegalArgumentException("Failed to load strands.");
            }

            return strands.stream()
                    .map(StrandMapper::toDTO)
                    .toList();
        });
    }

    public CompletableFuture<Boolean> createStrand(StrandDTO strandDTO) {
        validateSession();

        if (strandDTO.getStrandCode().trim().isEmpty() || strandDTO.getDescription().trim().isEmpty() || strandDTO.getTrackId() <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("All fields must not be empty.")
            );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var strand = StrandMapper.toNewModel(strandDTO);

        return CompletableFuture.supplyAsync(() -> {
            Strand existingStrand = _repo.findStrandByCode(strand.getStrandCode());

            if (existingStrand != null) {
                if (!existingStrand.isArchived()) {
                    throw new IllegalArgumentException("Strand code is already in use.");
                } else {
                    return _repo.restoreAndUpdateStrand(strand);
                }
            }

            if (_repo.existsByDescription(strand.getStrandDescription(), -1)) {
                throw new IllegalArgumentException("Strand description is already in use.");
            }

            Integer newStrandId = _repo.addStrand(strand);

            if (newStrandId == null) {
                return false;
            }

            logActivity(newStrandId.toString(), AuditAction.ADD, AuditModule.STRAND_MANAGEMENT, "Added new strand: " + newStrandId);

            return true;
        });
    }

    public CompletableFuture<Boolean> updateStrand(StrandDTO strandDTO) {
        validateSession();

        if (strandDTO == null) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid strand.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var strand = StrandMapper.toModel(strandDTO);

        return CompletableFuture.supplyAsync(() -> {
            Strand existingStrand = _repo.findStrandByCode(strand.getStrandCode());

            if (existingStrand != null && existingStrand.getStrandId() != strand.getStrandId()) {
                throw new IllegalArgumentException("Strand code is already in use.");
            }

            if (_repo.existsByDescription(strand.getStrandDescription(), strand.getStrandId())) {
                throw new IllegalArgumentException("Strand description is already in use.");
            }

            if (_repo.updateStrandById(strand)) {
                logActivity(String.valueOf(strand.getStrandId()), AuditAction.UPDATE, AuditModule.STRAND_MANAGEMENT, "Updated strand: " + strand.getStrandId());
                return true;
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> deleteStrand(int strandId) {
        validateSession();

        if (strandId <= 0) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid strand.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        return CompletableFuture.supplyAsync(() -> _repo.deleteStrandById(strandId));
    }
}
