package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.BatchAuditDTO;
import com.enrollmentsystem.dtos.BatchRecordDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.models.Section;
import com.enrollmentsystem.models.Strand;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.BatchSectioningRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.SectionRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BatchSectioningService extends BaseService {
    private final BatchSectioningRepository _batchRepo;
    private final SchoolYearRepository _syRepo;
    private final SectionRepository _secRepo;

    public BatchSectioningService(BatchSectioningRepository batchRepo, AuditRepository auditRepo, SchoolYearRepository syRepo, SectionRepository secRepo) {
        super(auditRepo);
        _batchRepo = batchRepo;
        _syRepo = syRepo;
        _secRepo = secRepo;
    }
    
    public CompletableFuture<BatchAuditDTO> getBatchSectionAudit() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var schoolYearId = _syRepo.getActiveSchoolYearId(conn);
                if (schoolYearId == null) throw new IllegalArgumentException("Failed to get active school year");

                var schoolYear = _syRepo.getSchoolYearById(conn, schoolYearId);
                if (schoolYear == null) throw new IllegalArgumentException("Failed to get active school year");

                var academicYear = "S.Y. " + schoolYear.getStartYear() + "-" + schoolYear.getEndYear();

                var batchAudit = _batchRepo.getBatchAudit(conn, academicYear);
                if (batchAudit == null) return null;

                batchAudit.setSchoolYear(academicYear);
                batchAudit.setSchoolYearId(schoolYearId);

                return batchAudit;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to check batch sectioned.");
            }
        });
    }

    public CompletableFuture<String> runBatchSectioning(int targetCapacity) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        if (targetCapacity <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid target capacity")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                Integer schoolYearId = _syRepo.getActiveSchoolYearId(conn);
                if (schoolYearId == null) throw new RuntimeException("Failed to get students in current school year.");

                var activeSchoolYear = _syRepo.getSchoolYearById(conn, schoolYearId);
                if (activeSchoolYear == null) throw new RuntimeException("Failed to get students in current school year.");

                if (_secRepo.checkSectionsExists(conn, schoolYearId)) {
                    _batchRepo.resetEnrollmentSections(conn, schoolYearId);
                    _batchRepo.deleteCurrentSections(conn, schoolYearId);
                }

                List<Strand> strands = _batchRepo.getAllStrandIds(conn);
                List<BatchRecordDTO> allEnrolledStudents = _batchRepo.getAllEnrolledStudents(conn, schoolYearId);

                try {
                    for (Strand strand : strands) {
                        for (int gradeLevel = 11; gradeLevel <= 12; gradeLevel++) {
                            final int currentGrade = gradeLevel;

                            List<BatchRecordDTO> currentBucket = allEnrolledStudents.stream()
                                    .filter(e -> e.getStrandId() == strand.getStrandId())
                                    .filter(e -> e.getGradeLevel() == currentGrade)
                                    .toList();

                            if (currentBucket.isEmpty()) continue;

                            int totalStudents = currentBucket.size();
                            int baseSections = totalStudents / targetCapacity;
                            int remainder = totalStudents % targetCapacity;
                            int sectionCount = (remainder >= 10) ? baseSections + 1 : Math.max(1, baseSections);

                            List<Section> initialSections = new ArrayList<>();

                            for (int sectionNameIndex = 0; sectionNameIndex < sectionCount; sectionNameIndex++) {
                                var sec = new Section();
                                sec.setName(strand.getStrandCode() + " " + gradeLevel + "-" + (sectionNameIndex + 1));
                                sec.setSchoolYearId(schoolYearId);
                                sec.setStrandId(strand.getStrandId());
                                sec.setRoomAssignment("TBA");

                                initialSections.add(sec);
                            }

                            var sectionGeneratedIds = _batchRepo.addSectionsAndGenerateIds(conn, initialSections);
                            if (sectionGeneratedIds.isEmpty()) throw new RuntimeException("Failed to save sections");

                            List<BatchRecordDTO> males = currentBucket.stream()
                                    .filter(e -> e.getGender() == Gender.MALE)
                                    .toList();

                            List<BatchRecordDTO> females = currentBucket.stream()
                                    .filter(e -> e.getGender() == Gender.FEMALE)
                                    .toList();

                            int pointer = 0;

                            for (BatchRecordDTO record : males) {
                                int index = pointer % sectionCount;
                                record.setSectionId(sectionGeneratedIds.get(index));
                                pointer++;
                            }

                            for (BatchRecordDTO record : females) {
                                int index = pointer % sectionCount;
                                record.setSectionId(sectionGeneratedIds.get(index));
                                pointer++;
                            }

                            _batchRepo.saveUpdatedRecords(conn, currentBucket);
                        }
                    }

                    String currentSchoolYear = "S.Y. " + activeSchoolYear.getStartYear() + "-" + activeSchoolYear.getEndYear();

                    logActivity(
                            conn,
                            currentSchoolYear,
                            AuditAction.RUN_BATCH_SECTIONING,
                            AuditModule.RUN_BATCH_SECTIONING,
                            "Automated sectioning completed for all strands in " + currentSchoolYear
                    );

                    conn.commit();
                    return currentSchoolYear;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to create sections.");
            }
        });
    }
}
