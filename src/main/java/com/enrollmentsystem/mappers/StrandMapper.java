package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.models.Strand;

public class StrandMapper {
    public static Strand toNewModel(StrandDTO dto) {
        var strand = new Strand();
        strand.setTrackId(dto.getTrackId());
        strand.setStrandCode(dto.getStrandCode());
        strand.setStrandDescription(dto.getDescription());
        strand.setArchived(false);

        return strand;
    }

    public static Strand toModel(StrandDTO dto) {
        var strand = new Strand();
        strand.setTrackId(dto.getTrackId());
        strand.setStrandId(dto.getStrandId());
        strand.setStrandCode(dto.getStrandCode());
        strand.setStrandDescription(dto.getDescription());
        strand.setArchived(dto.isArchived());

        return strand;
    }

    public static StrandDTO toDTO(Strand strand) {
        var dto = new StrandDTO();
        dto.setTrackId(strand.getTrackId());
        dto.setStrandId(strand.getStrandId());
        dto.setTrackCode(strand.getTrackCode());
        dto.setStrandCode(strand.getStrandCode());
        dto.setDescription(strand.getStrandDescription());
        dto.setArchived(strand.isArchived());

        return dto;
    }
}
