package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.viewmodels.enrollment.addstudent.LearnerInformationViewModel;

public class StudentMapper {
    public static Student toModel(EnrollmentFormDTO dto) {
        var student = new Student();
        student.setLrn(dto.getLrn());
        student.setLastName(dto.getLastName());
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setExtensionName(dto.getExtensionName());
        student.setBirthDate(dto.getBirthDate());
        student.setAge(dto.getAge());
        student.setSex(dto.getSex());

        return student;
    }

    public static void mapToFormDTO(LearnerInformationViewModel vm, EnrollmentFormDTO dto) {
        dto.setLrn(vm.lrnProperty().get());
        dto.setLastName(vm.lastNameProperty().get());
        dto.setFirstName(vm.firstNameProperty().get());
        dto.setMiddleName(vm.middleNameProperty().get());
        dto.setExtensionName(vm.extensionNameProperty().get());
        dto.setBirthDate(vm.birthDateProperty().get());
        dto.setAge(vm.ageProperty().get());
        dto.setSex(vm.sexProperty().get());
    }
}
