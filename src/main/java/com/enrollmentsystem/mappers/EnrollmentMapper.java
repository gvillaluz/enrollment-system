package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.models.Enrollment;

import java.time.LocalDate;

public class EnrollmentMapper {
    public static Enrollment toNewModel(EnrollmentFormDTO dto) {
        var enrollment = new Enrollment();
        enrollment.setStudentLrn(dto.getLrn());
        enrollment.setSectionId(dto.getSectionId() == 0 ? null : dto.getSectionId());
        enrollment.setSchoolYearId(dto.getSchoolYearId());
        enrollment.setSemTerm(dto.getSemester());
        enrollment.setTrackId(dto.getTrackId());
        enrollment.setStrandId(dto.getStrandId());
        enrollment.setGradeLevel(dto.getGradeLevel());
        enrollment.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
        enrollment.setDateEnrolled(LocalDate.now());

        return enrollment;
    }

    public static Enrollment toExistingModel(EnrollmentFormDTO dto) {
        var enrollment = new Enrollment();
        enrollment.setEnrollmentId(dto.getEnrollmentId());
        enrollment.setStudentLrn(dto.getLrn());
        enrollment.setSectionId(dto.getSectionId());
        enrollment.setSchoolYearId(dto.getSchoolYearId());
        enrollment.setSemTerm(dto.getSemester());
        enrollment.setTrackId(dto.getTrackId());
        enrollment.setStrandId(dto.getStrandId());
        enrollment.setGradeLevel(dto.getGradeLevel());
        enrollment.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
        enrollment.setDateEnrolled(LocalDate.now());

        return enrollment;
    }
}
