package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.SectionRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SectionService extends BaseService {
    private final SectionRepository _sectionRepo;
    private final SchoolYearRepository _syRepo;

    public SectionService(SectionRepository repo, SchoolYearRepository syRepo, AuditRepository auditRepo) {
        _sectionRepo = repo;
        _syRepo = syRepo;
        super(auditRepo);
    }

    public CompletableFuture<List<SectionDTO>> getAllSections() {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                Integer syId = _syRepo.getActiveSchoolYearId(conn);

                if (syId == null) {
                    throw new IllegalArgumentException("Failed to load sections");
                }

                return _sectionRepo.getAllSections(conn, syId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get sections.");
            }
        });
    }

    public CompletableFuture<Boolean> updateSection(SectionDTO dto) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                Section section = _sectionRepo.getSectionById(conn, dto.getSectionId());
                if (section == null) throw new IllegalArgumentException("Section not found.");

                var modifiedFields = getModifiedFieldsCount(section, dto);

                section.setName(dto.getSectionName());
                section.setStrandId(dto.getStrandId());
                section.setRoomAssignment(dto.getRoomAssignment());

                try {
                    _sectionRepo.updateSectionById(conn, section);
                    logActivity(
                            conn,
                            section.getName(),
                            AuditAction.UPDATE,
                            AuditModule.SECTION_MANAGEMENT,
                            "Modified " + modifiedFields.size() + " fields: " + String.join(", ", modifiedFields)
                    );

                    conn.commit();
                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update section.");
            }
        });
    }

    private List<String> getModifiedFieldsCount(Section oldObj, SectionDTO newObj) {
        var modifiedFields = new ArrayList<String>();

        if (!oldObj.getName().equals(newObj.getSectionName()))
            modifiedFields.add("Name");

        if (!oldObj.getRoomAssignment().equalsIgnoreCase(newObj.getRoomAssignment()))
            modifiedFields.add("Room Assignment");

        if (oldObj.getStrandId() != newObj.getStrandId())
            modifiedFields.add("Strand");

        return modifiedFields;
    }
}
