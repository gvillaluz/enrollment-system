package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.EnrollmentDTO;
import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.mappers.*;
import com.enrollmentsystem.models.*;
import com.enrollmentsystem.repositories.*;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnrollmentService extends BaseService {
    private final EnrollmentRepository _enrollmentRepo;
    private final StudentRepository _studentRepo;
    private final AcademicRepository _academicRepo;
    private final RequirementRepository _requirementRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo, StudentRepository studentRepo, AcademicRepository academicRepo, RequirementRepository requirementRepo, AuditRepository auditRepo) {
        super(auditRepo);
        _enrollmentRepo = enrollmentRepo;
        _studentRepo = studentRepo;
        _academicRepo = academicRepo;
        _requirementRepo = requirementRepo;
    }

    public CompletableFuture<Integer> countRows(int gradeLevel, String searchValue) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                Integer schoolYearId = _enrollmentRepo.getActiveSchoolYearId(conn);

                if (schoolYearId == null) {
                    System.out.println("Failed to get active school year: ");
                    throw new IllegalArgumentException("Failed to get enrollment list.");
                }

                return _enrollmentRepo.getRowsCount(conn, gradeLevel, schoolYearId, searchValue);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get row count.");
            }
        });
    }

    public CompletableFuture<List<EnrollmentDTO>> loadLatest20Enrollments(int gradeLevel, String searchValue, int offset) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                Integer schoolYearId = _enrollmentRepo.getActiveSchoolYearId(conn);

                if (schoolYearId == null) {
                    System.out.println("Failed to get active school year: ");
                    throw new IllegalArgumentException("Failed to get enrollment list.");
                }

                return _enrollmentRepo.getLatest20(conn, gradeLevel, schoolYearId, searchValue, offset);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get latest enrollment records.");
            }
        });
    }

    public CompletableFuture<EnrollmentFormDTO> loadEnrollmentDataByLRN(String lrn) {
        validateSession();

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var student = _studentRepo.findStudentByLRN(conn, lrn);

                if (student == null) {
                    return null;
                }

                EnrollmentFormDTO enrollmentFormDTO = _enrollmentRepo.getEnrollmentDataByLRN(conn, student.getLrn());

                enrollmentFormDTO.setRequirementDTOS(
                        RequirementMapper.toDTOListFromModels(_requirementRepo.getStudentRequirementsByLRN(conn, enrollmentFormDTO.getLrn()))
                );

                return enrollmentFormDTO;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load enrollment record.");
            }
        });
    }

    public CompletableFuture<EnrollmentFormDTO> loadEnrollmentData(int enrollmentId) {
        validateSession();

        if (enrollmentId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid enrollment.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                EnrollmentFormDTO enrollmentFormDTO = _enrollmentRepo.getEnrollmentEditData(conn, enrollmentId);

                System.out.println("Service: " + enrollmentFormDTO.getSemester());

                enrollmentFormDTO.setRequirementDTOS(
                        RequirementMapper.toDTOListFromModels(_requirementRepo.getStudentRequirementsByLRN(conn, enrollmentFormDTO.getLrn()))
                );

                return enrollmentFormDTO;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load enrollment record.");
            }
        });

    }

    public CompletableFuture<Boolean> processEnrollment(EnrollmentFormDTO dto) {
        try {
            validateEnrollmentFields(dto);
            validateSession();
        } catch (IllegalArgumentException e) {
            return CompletableFuture.failedFuture(e);
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    Student student = StudentMapper.toModel(dto);
                    AcademicInformation academicInformation = AcademicMapper.toNewModel(dto);
                    List<StudentRequirement> requirements = RequirementMapper.toNewModels(dto.getRequirementDTOS());

                    Integer activeSchoolYrId = _enrollmentRepo.getActiveSchoolYearId(conn);

                    if (activeSchoolYrId == null) {
                        throw new IllegalArgumentException("No active school year.");
                    }

                    Enrollment enrollment = EnrollmentMapper.toNewModel(dto);
                    enrollment.setSchoolYearId(activeSchoolYrId);
                    enrollment.setUserId(UserSession.getInstance().getUser().getUserId());

                    if (_enrollmentRepo.checkEnrollmentExists(conn, enrollment.getStudentLrn(), enrollment.getSemTerm(), activeSchoolYrId)) {
                        throw new IllegalArgumentException("Student has already been enrolled in this school year.");
                    }

                    if (!_studentRepo.checkStudentExistsByLRN(conn, dto.getLrn())) {
                        _studentRepo.addStudent(conn, student);
                        _academicRepo.addAcademicInformation(conn, academicInformation);
                        _requirementRepo.addStudentRequirements(conn, requirements);

                        logActivity(
                                conn,
                                student.getLrn(),
                                AuditAction.ADD,
                                AuditModule.ENROLLMENT,
                                "Added student: " + student.getLrn()
                        );
                    } else {
                        _studentRepo.updateStudent(conn, student);
                        _requirementRepo.updateStudentRequirements(conn, requirements);

                        logActivity(
                                conn,
                                student.getLrn(),
                                AuditAction.UPDATE,
                                AuditModule.ENROLLMENT,
                                "Updated student: " + student.getLrn()
                        );
                    }

                    _enrollmentRepo.addEnrollment(conn, enrollment);

                    logActivity(
                            conn,
                            student.getLrn(),
                            AuditAction.ADD,
                            AuditModule.ENROLLMENT,
                            "Added enrollment record: " + student.getLrn()
                    );

                    conn.commit();
                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Failed to process enrollment: " + e.getMessage());
                throw new IllegalArgumentException("Failed to process enrollment");
            }
        });
    }

    public CompletableFuture<Boolean> editEnrollment(EnrollmentFormDTO dto) {
        validateSession();

        try {
            validateEnrollmentFields(dto);
        } catch (IllegalArgumentException e) {
            return CompletableFuture.failedFuture(e);
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    Student student = StudentMapper.toModel(dto);
                    AcademicInformation academicInformation = AcademicMapper.toExistingModel(dto);
                    List<StudentRequirement> requirements = RequirementMapper.toExistingModels(dto.getRequirementDTOS());
                    Enrollment enrollment = EnrollmentMapper.toExistingModel(dto);

                    _studentRepo.updateStudent(conn, student);
                    _academicRepo.updateAcademicInformation(conn, academicInformation);
                    _requirementRepo.updateStudentRequirements(conn, requirements);
                    _enrollmentRepo.updateEnrollment(conn, enrollment);

                    logActivity(
                            conn,
                            student.getLrn(),
                            AuditAction.UPDATE,
                            AuditModule.ENROLLMENT,
                            "Updated enrollment: " + student.getFirstName() + " " + student.getLastName()
                    );

                    conn.commit();

                    return true;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("Failed to edit enrollment: " + e.getMessage());
                throw new IllegalArgumentException("Failed to edit enrollment");
            }
        });
    }

    public CompletableFuture<Boolean> archiveEnrollmentRecord(int enrollmentId, String lrn) {
        validateSession();

        if (enrollmentId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Student LRN")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                int rowsAffected = _enrollmentRepo.archiveEnrollmentByLRN(conn, enrollmentId);

                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        lrn,
                        AuditAction.ARCHIVE,
                        AuditModule.ENROLLMENT,
                        "Archive enrollment record: " + lrn);

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to archive enrollment record.");
            }
        });
    }

    private void validateEnrollmentFields(EnrollmentFormDTO dto) {
        if (isInvalid(dto.getLrn()) ||
                isInvalid(dto.getFirstName()) ||
                isInvalid(dto.getLastName()) ||
                dto.getBirthDate() == null ||
                dto.getSex() == null) {
            throw new IllegalArgumentException("Mandatory Learner Information is missing.");
        }

        if (dto.getSemester() == null ||
                dto.getTrackId() <= 0 ||
                dto.getStrandId() <= 0) {
            throw new IllegalArgumentException("Mandatory Academic Information is missing.");
        }
    }

    private boolean isInvalid(String str) {
        return str == null || str.trim().isEmpty();
    }
}
