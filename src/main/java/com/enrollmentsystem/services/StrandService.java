package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.StrandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StrandService {
    private final StrandRepository _repo;

    public StrandService(StrandRepository repository) {
        this._repo = repository;
    }

    public CompletableFuture<List<StrandDTO>> loadStrands() {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            List<StrandDTO> strandDTOs = new ArrayList<>();
            var strands = _repo.getAllStrands();

            if (strands == null) {
                throw new IllegalArgumentException("Failed to load strands.");
            }

            for (Strand strand : strands) {
                strandDTOs.add(new StrandDTO(
                        strand.getStrandId(),
                        strand.getStrandCode(),
                        strand.getStrandDescription(),
                        strand.getTrackId(),
                        strand.getTrackCode(),
                        strand.isArchived()
                ));
            }

            return strandDTOs;
        });
    }

    public CompletableFuture<Boolean> createStrand(StrandDTO strandDTO) {
        if (strandDTO.getStrandCode().trim().isEmpty() || strandDTO.getDescription().trim().isEmpty() || strandDTO.getTrackId() <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("All fields must not be empty.")
            );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var strand = new Strand();
        strand.setStrandCode(strandDTO.getStrandCode());
        strand.setStrandDescription(strandDTO.getDescription());
        strand.setTrackId(strandDTO.getTrackId());
        strand.setArchived(false);

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

            return _repo.addStrand(strand);
        });
    }

    public CompletableFuture<Boolean> updateStrand(StrandDTO strandDTO) {
        if (strandDTO == null) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid strand.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        var strand = new Strand();
        strand.setStrandId(strandDTO.getStrandId());
        strand.setStrandCode(strandDTO.getStrandCode());
        strand.setStrandDescription(strandDTO.getDescription());
        strand.setTrackId(strandDTO.getTrackId());

        return CompletableFuture.supplyAsync(() -> {
            Strand existingStrand = _repo.findStrandByCode(strand.getStrandCode());

            if (existingStrand != null && existingStrand.getStrandId() != strand.getStrandId()) {
                throw new IllegalArgumentException("Strand code is already in use.");
            }

            if (_repo.existsByDescription(strand.getStrandDescription(), strand.getStrandId())) {
                throw new IllegalArgumentException("Strand description is already in use.");
            }

            return _repo.updateStrandById(strand);
        });
    }

    public CompletableFuture<Boolean> deleteStrand(int strandId) {
        if (strandId <= 0) return CompletableFuture.failedFuture(
                new IllegalArgumentException("Invalid strand.")
        );

        if (!UserSession.getInstance().isAdmin()) return CompletableFuture.failedFuture(
                new SecurityException("Unauthorized access.")
        );

        return CompletableFuture.supplyAsync(() -> _repo.deleteStrandById(strandId));
    }
}
