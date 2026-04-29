package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.enums.SchoolYearStatus;
import com.enrollmentsystem.models.SchoolYear;
import com.enrollmentsystem.viewmodels.academic.schoolyear.SchoolYearViewModel;

public class SchoolYearMapper {
    public static SchoolYear toNewModel(SchoolYearDTO dto) {
        var sy = new SchoolYear();
        sy.setStartYear(dto.getStartYear());
        sy.setEndYear(dto.getEndYear());
        sy.setStatus(SchoolYearStatus.OPEN);

        return sy;
    }

    public static SchoolYear toModel(SchoolYearDTO dto) {
        var sy = new SchoolYear();
        sy.setSchoolYearId(dto.getSchoolYearId());
        sy.setStartYear(dto.getStartYear());
        sy.setEndYear(dto.getEndYear());
        sy.setStatus(dto.getStatus());

        return sy;
    }

    public static SchoolYearDTO toDTO(SchoolYear sy) {
        var dto = new SchoolYearDTO();
        dto.setSchoolYearId(sy.getSchoolYearId());
        dto.setStartYear(sy.getStartYear());
        dto.setEndYear(sy.getEndYear());
        dto.setStatus(sy.getStatus());

        return dto;
    }

    public static SchoolYearDTO vmToDTO(SchoolYearViewModel vm) {
        var dto = new SchoolYearDTO();
        dto.setSchoolYearId(vm.schoolYearIdProperty().get());
        dto.setStartYear(vm.startYearProperty().get());
        dto.setEndYear(vm.endYearProperty().get());
        dto.setStatus(vm.statusProperty().get());

        return dto;
    }
}
