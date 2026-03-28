package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.SectionRepository;

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

    public CompletableFuture<List<SectionDTO>> loadSections() {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            List<SectionDTO> dtos = new ArrayList<>();

            Integer syId = _syRepo.getActiveSchoolYearId();

            if (syId == null) {
                throw new IllegalArgumentException("Failed to load sections");
            }

            var sections = _sectionRepo.getAllSections(syId);

            for (Section s : sections) {
                var sec = new SectionDTO();
                sec.setSectionId(s.getSectionId());
                sec.setSectionName(s.getName());
                sec.setMaxCapacity(s.getMaxCap());
                sec.setStrandId(s.getStrandId());
                sec.setSchoolYearId(s.getSchoolYearId());
                sec.setRoomAssignment(s.getRoomAssignment());

                dtos.add(sec);
            }

            return dtos;
        });
    }
}
