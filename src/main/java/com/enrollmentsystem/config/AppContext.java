package com.enrollmentsystem.config;

import com.enrollmentsystem.models.User;
import com.enrollmentsystem.repositories.*;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.services.*;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AppContext {
    private static AcademicRepository academicRepository;

    private static DashboardRepository dashboardRepository;
    private static UserRepository userRepository;
    private static StudentRepository studentRepository;
    private static EnrollmentRepository enrollmentRepository;
    private static TrackRepository trackRepository;
    private static StrandRepository strandRepository;
    private static AuditRepository auditRepository;
    private static SectionRepository sectionRepository;
    private static SchoolYearRepository schoolYearRepository;
    private static RequirementRepository requirementRepository;
    private static ArchiveRepository archiveRepository;
    private static BatchSectioningRepository batchSectioningRepository;

    private static DashboardService dashboardService;
    private static AuthService authService;
    private static UserService userService;
    private static StudentService studentService;
    private static EnrollmentService enrollmentService;
    private static TrackService trackService;
    private static StrandService strandService;
    private static AuditService auditService;
    private static SectionService sectionService;
    private static RequirementService requirementService;
    private static ArchiveService archiveService;
    private static SchoolYearService schoolYearService;
    private static BatchSectioningService batchSectioningService;
    private static ClasslistService classlistService;

    private static DashboardRepository getDashboardRepository() {
        if (dashboardRepository == null) {
            dashboardRepository = new DashboardRepository();
        }
        return dashboardRepository;
    }

    public static DashboardService getDashboardService() {
        if (dashboardService == null) {
            dashboardService = new DashboardService(
                    getDashboardRepository(),
                    getSchoolYearRepository()
            );
        }
        return dashboardService;
    }

    public static AcademicRepository getAcademicRepository() {
        if (academicRepository == null) {
            academicRepository = new AcademicRepository();
        }
        return academicRepository;
    }


    public static RequirementRepository getRequirementRepository() {
        if (requirementRepository == null) {
            requirementRepository = new RequirementRepository();
        }
        return requirementRepository;
    }

    public static RequirementService getRequirementService() {
        if (requirementService == null) {
            requirementService = new RequirementService(
                    getRequirementRepository(),
                    getSchoolYearRepository(),
                    getAuditRepository()
            );
        }
        return requirementService;
    }


    private static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService(
                    getUserRepository(),
                    getAuditRepository()
            );
        }

        return userService;
    }

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(
                    getUserRepository(),
                    getAuditRepository()
            );
        }
        return authService;
    }

    public static EnrollmentRepository getEnrollmentRepository() {
        if (enrollmentRepository == null) {
            enrollmentRepository = new EnrollmentRepository();
        }
        return enrollmentRepository;
    }

    public static EnrollmentService getEnrollmentService() {
        if (enrollmentService == null) {
            enrollmentService = new EnrollmentService(
                    getEnrollmentRepository(),
                    getStudentRepository(),
                    getAcademicRepository(),
                    getRequirementRepository(),
                    getAuditRepository()
            );
        }
        return enrollmentService;
    }


    public static StudentRepository getStudentRepository() {
        if (studentRepository == null) {
            studentRepository = new StudentRepository();
        }
        return studentRepository;
    }

    public static StudentService getStudentService() {
        if (studentService == null) {
            studentService = new StudentService(
                    getStudentRepository(),
                    getSchoolYearRepository()
            );
        }
        return studentService;
    }


    public static TrackRepository getTrackRepository() {
        if (trackRepository == null) {
            trackRepository = new TrackRepository();
        }
        return trackRepository;
    }

    public static TrackService getTrackService() {
        if (trackService == null) {
            trackService = new TrackService(
                    getTrackRepository(),
                    getAuditRepository()
            );
        }
        return trackService;
    }


    public static StrandRepository getStrandRepository() {
        if (strandRepository == null) {
            strandRepository = new StrandRepository();
        }
        return strandRepository;
    }

    public static StrandService getStrandService() {
        if (strandService == null) {
            strandService = new StrandService(
                    getStrandRepository(),
                    getAuditRepository()
            );
        }
        return strandService;
    }


    public static AuditRepository getAuditRepository() {
        if (auditRepository == null) {
            auditRepository = new AuditRepository();
        }
        return auditRepository;
    }

    public static AuditService getAuditService() {
        if (auditService == null) {
            auditService = new AuditService(getAuditRepository());
        }
        return auditService;
    }


    public static SectionRepository getSectionRepository() {
        if (sectionRepository == null) {
            sectionRepository = new SectionRepository();
        }
        return sectionRepository;
    }

    public static SectionService getSectionService() {
        if (sectionService == null) {
            sectionService = new SectionService(
                    getSectionRepository(),
                    getSchoolYearRepository(),
                    getAuditRepository()
            );
        }
        return sectionService;
    }

    public static SchoolYearRepository getSchoolYearRepository() {
        if (schoolYearRepository == null) {
            schoolYearRepository = new SchoolYearRepository();
        }
        return schoolYearRepository;
    }

    public static SchoolYearService getSchoolYearService() {
        if (schoolYearService == null) {
            schoolYearService = new SchoolYearService(
                    getSchoolYearRepository(),
                    getAuditRepository()
            );
        }
        return schoolYearService;
    }

    public static ArchiveRepository getArchiveRepository() {
        if (archiveRepository == null) {
            archiveRepository = new ArchiveRepository();
        }
        return archiveRepository;
    }

    public static ArchiveService getArchiveService() {
        if (archiveService == null) {
            archiveService = new ArchiveService(
                    getArchiveRepository(),
                    getAuditRepository(),
                    getSchoolYearRepository()
            );
        }
        return archiveService;
    }

    private static BatchSectioningRepository getBatchSectioningRepository() {
        if (batchSectioningRepository == null) {
            batchSectioningRepository = new BatchSectioningRepository();
        }
        return batchSectioningRepository;
    }

    public static BatchSectioningService getBatchSectioningService() {
        if (batchSectioningService == null) {
            batchSectioningService = new BatchSectioningService(
                    getBatchSectioningRepository(),
                    getAuditRepository(),
                    getSchoolYearRepository(),
                    getSectionRepository()
            );
        }
        return batchSectioningService;
    }

    public static ClasslistService getClasslistService() {
        if (classlistService == null) {
            classlistService = new ClasslistService(
                getEnrollmentRepository(),
                getSectionRepository(),
                getAuditRepository(),
                getSchoolYearRepository()
            );
        }
        return classlistService;
    }
}
