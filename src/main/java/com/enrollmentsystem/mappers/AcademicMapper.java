package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.models.AcademicInformation;
import com.enrollmentsystem.viewmodels.enrollment.AcademicInformationViewModel;

public class AcademicMapper {
    public static AcademicInformation toNewModel(EnrollmentFormDTO dto) {
        var academic = new AcademicInformation();
        academic.setStudentLrn(dto.getLrn());
        academic.setLastGradeLevel(dto.getLastGradeLevel());
        academic.setLastSchoolYear(dto.getLastSchoolYear());
        academic.setLastSchoolName(dto.getLastSchoolName());
        academic.setLastSchoolId(dto.getLastSchoolId());

        return academic;
    }

    public static AcademicInformation toExistingModel(EnrollmentFormDTO dto) {
        var academic = new AcademicInformation();
        academic.setAcademicInformationId(dto.getAcademicInformationId());
        academic.setStudentLrn(dto.getLrn());
        academic.setLastGradeLevel(dto.getLastGradeLevel());
        academic.setLastSchoolYear(dto.getLastSchoolYear());
        academic.setLastSchoolName(dto.getLastSchoolName());
        academic.setLastSchoolId(dto.getLastSchoolId());

        return academic;
    }

    public static void mapToFormDTO(AcademicInformationViewModel vm, EnrollmentFormDTO dto) {
        dto.setAcademicInformationId(vm.academicInformationIdProperty().get());
        dto.setLastGradeLevel(vm.lastGrLevelProperty().get());
        dto.setLastSchoolYear(vm.lastSchoolYearProperty().get());
        dto.setLastSchoolName(vm.lastSchoolAttendedProperty().get());
        dto.setLastSchoolId(vm.lastSchoolIdProperty().get());
        dto.setSemester(vm.semesterProperty().get());
        dto.setTrackId(vm.getTrackId());
        dto.setStrandId(vm.getStrandId());
        dto.setSectionId(vm.sectionIdProperty().get());
        dto.setSectionName(vm.sectionNameProperty().get());
    }
}
