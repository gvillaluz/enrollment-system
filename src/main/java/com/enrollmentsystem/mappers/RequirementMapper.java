package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.StudentRequirementDTO;
import com.enrollmentsystem.models.StudentRequirement;
import com.enrollmentsystem.viewmodels.enrollment.StudentRequirementViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequirementMapper {
    public static List<StudentRequirement> toExistingModels(List<StudentRequirementDTO> dtoList) {
        List<StudentRequirement> requirements = new ArrayList<>();

        for (StudentRequirementDTO dto : dtoList) {
            var student = new StudentRequirement();
            student.setStudentRequirementId(dto.getStudentRequirementId());
            student.setStudentLrn(dto.getStudentLrn());
            student.setRequirementReferenceId(dto.getRequirementRefId());
            student.setSubmitted(dto.isSubmitted());
            student.setDateSubmitted(dto.getDateSubmitted());

            requirements.add(student);
        }
        return requirements;
    }

    public static List<StudentRequirement> toNewModels(List<StudentRequirementDTO> dtoList) {
        List<StudentRequirement> requirements = new ArrayList<>();

        for (StudentRequirementDTO dto : dtoList) {
            var student = new StudentRequirement();
            student.setStudentLrn(dto.getStudentLrn());
            student.setRequirementReferenceId(dto.getRequirementRefId());
            student.setSubmitted(dto.isSubmitted());
            student.setDateSubmitted(dto.isSubmitted() ? LocalDate.now() : null);

            requirements.add(student);
        }
        return requirements;
    }

    public static StudentRequirementDTO toDTO(StudentRequirement model) {
        return new StudentRequirementDTO(
                model.getStudentRequirementId(),
                model.getStudentLrn(),
                model.getRequirementReferenceId(),
                model.isSubmitted(),
                model.getDateSubmitted()
        );
    }

    public static List<StudentRequirementDTO> toDTOListFromModels(List<StudentRequirement> models) {
        return models.stream()
                .map(RequirementMapper::toDTO)
                .collect(Collectors.toList());
    }

    private static StudentRequirementDTO toDTO(StudentRequirementViewModel vm) {
        return new StudentRequirementDTO(
                vm.studentRequirementIdProperty().get(),
                vm.studentLrnProperty().get(),
                vm.requirementRefIdProperty().get(),
                vm.submittedProperty().get(),
                vm.dateSubmittedProperty().get()
        );
    }

    public static List<StudentRequirementDTO> toDTOList(Map<Integer, StudentRequirementViewModel> requirementMap) {
        return requirementMap.values().stream()
                .map(RequirementMapper::toDTO)
                .collect(Collectors.toList());
    }
}
