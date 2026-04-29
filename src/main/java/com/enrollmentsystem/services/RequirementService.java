package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.RequirementNoteDTO;
import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.RequirementMapper;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.RequirementRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.ValidationHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
            try (Connection conn = DatabaseConnection.getConnection()) {
                int schoolYearId = _syRepo.getActiveSchoolYearId(conn);

                if (schoolYearId <= 0) {
                    System.out.println("No active school year id");
                    throw new IllegalArgumentException("Failed to load checklist");
                }

                return _requirementRepo.getRequirementRowCount(conn, schoolYearId, searchValue);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get row count.");
            }
        });
    }

    public CompletableFuture<List<RequirementNoteDTO>> getRequirementNotes(String lrn) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _requirementRepo.getRequirementNotesByLRN(conn, lrn);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load requirement notes.");
            }
        });
    }

    public CompletableFuture<List<RequirementSummaryDTO>> getRequirementsChecklist(String searchValue, int offset) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int schoolYearId = _syRepo.getActiveSchoolYearId(conn);

                if (schoolYearId <= 0) {
                    System.out.println("No active school year id");
                    throw new IllegalArgumentException("Failed to load checklist");
                }

                return _requirementRepo.getAllChecklist(conn, schoolYearId, searchValue, offset);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get requirements.");
            }
        });
    }

    public CompletableFuture<Void> updateStudentRequirement(String lrn, int refId, boolean isSubmitted) {
        validateSession();

        if (ValidationHelper.isNullOrEmpty(lrn) || refId == 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid requirement.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int rowsAffected = _requirementRepo.updateRequirement(conn, lrn, refId, isSubmitted);

                if (rowsAffected > 0)
                    logActivity(
                            conn,
                            lrn,
                            AuditAction.UPDATE,
                            AuditModule.REQUIREMENTS,
                            "Updated student requirement: " + lrn
                    );

                return null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update requirement.");
            }
        });
    }

    public CompletableFuture<RequirementNoteDTO> createRequirementNote(RequirementNoteDTO note) {
        validateSession();

        if (note == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Note.")
            );
        }

        note.setDateAdded(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    int generatedId = _requirementRepo.addRequirementNote(conn, RequirementMapper.toNewNoteModel(note));
                    if (generatedId == 0) throw new RuntimeException("Failed to add requirement note.");

                    logActivity(
                            conn,
                            note.getLrn(),
                            AuditAction.ADD,
                            AuditModule.REQUIREMENTS,
                            "Added note for student: " + note.getLrn()
                    );

                    conn.commit();

                    note.setRequirementNoteId(generatedId);
                    return note;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
                throw new RuntimeException("Failed to add requirement note.");
            }
        });
    }

    public CompletableFuture<RequirementNoteDTO> updateRequirementNote(RequirementNoteDTO note) {
        validateSession();

        if (note == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Note.")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                var existingNote = _requirementRepo.getRequirementNoteById(conn, note.getRequirementNoteId());
                if (existingNote == null) throw new IllegalArgumentException("Record not found.");

                var updatedDate = LocalDateTime.now();

                existingNote.setNote(note.getNote());
                existingNote.setUserId(note.getUserId());
                existingNote.setUpdatedAt(updatedDate);
                note.setUpdatedAt(updatedDate);

                try {
                    _requirementRepo.updateRequirementNote(conn, existingNote);

                    logActivity(
                            conn,
                            note.getLrn(),
                            AuditAction.UPDATE,
                            AuditModule.REQUIREMENTS,
                            "Updated requirement note for student: " + note.getLrn()
                    );

                    conn.commit();
                    return note;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
                throw new RuntimeException("Failed to update requirement note.");
            }
        });
    }

    public CompletableFuture<Boolean> markAsResolvedNote(int noteId, String lrn) {
        validateSession();

        if (noteId <= 0 || lrn == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Note.")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    _requirementRepo.updateNoteResolve(conn, noteId);

                    logActivity(
                            conn,
                            lrn,
                            AuditAction.UPDATE,
                            AuditModule.REQUIREMENTS,
                            "Note resolved for student: " + lrn
                    );

                    conn.commit();
                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
                throw new RuntimeException("Failed to mark note as resolved.");
            }
        });
    }

    public CompletableFuture<Boolean> undoNoteResolved(int noteId, String lrn) {
        validateSession();

        if (noteId <= 0 || lrn == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Note.")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    _requirementRepo.undoResolve(conn, noteId);

                    logActivity(
                            conn,
                            lrn,
                            AuditAction.UPDATE,
                            AuditModule.REQUIREMENTS,
                            "Undo Resolution: Note is now pending for Student LRN " + lrn
                    );

                    conn.commit();
                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
                throw new RuntimeException("Failed to undo note resolution.");
            }
        });
    }
}
