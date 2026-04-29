package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.StrandMapper;
import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.StrandRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
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
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _repo.getAllStrands(conn).stream()
                        .map(StrandMapper::toDTO)
                        .toList();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load strands.");
            }
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
            try (Connection conn = DatabaseConnection.getConnection()) {
                Strand existingStrand = _repo.findStrandByCode(conn, strand.getStrandCode());

                if (existingStrand != null) {
                    if (!existingStrand.isArchived()) {
                        throw new IllegalArgumentException("Strand code is already in use.");
                    } else {
                        return _repo.restoreAndUpdateStrand(conn, strand) != 0;
                    }
                }

                if (_repo.existsByDescription(conn, strand.getStrandDescription(), -1)) {
                    throw new IllegalArgumentException("Strand description is already in use.");
                }

                _repo.addStrand(conn, strand);

                logActivity(
                        conn,
                        strand.getStrandCode(),
                        AuditAction.ADD,
                        AuditModule.STRAND_MANAGEMENT,
                        "Added new strand: " + strand.getStrandCode()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to add strand.");
            }
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
            try (Connection conn = DatabaseConnection.getConnection()) {
                Strand existingStrand = _repo.findStrandByCode(conn, strand.getStrandCode());

                if (existingStrand != null && existingStrand.getStrandId() != strand.getStrandId()) {
                    throw new IllegalArgumentException("Strand code is already in use.");
                }

                if (_repo.existsByDescription(conn, strand.getStrandDescription(), strand.getStrandId())) {
                    throw new IllegalArgumentException("Strand description is already in use.");
                }

                int rowsAffected = _repo.updateStrandById(conn, strand);
                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        strand.getStrandCode(),
                        AuditAction.UPDATE,
                        AuditModule.STRAND_MANAGEMENT,
                        "Updated strand: " + strand.getStrandCode()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update strand.");
            }
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

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int rowsAffected = _repo.deleteStrandById(conn, strandId);
                return rowsAffected != 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to delete strand.");
            }
        });
    }
}
