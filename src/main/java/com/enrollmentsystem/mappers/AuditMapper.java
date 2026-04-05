package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.AuditDTO;
import com.enrollmentsystem.models.AuditLog;

public class AuditMapper {
    public static AuditLog toModel(AuditDTO dto) {
        var audit = new AuditLog();
        audit.setLogId(dto.getLogId());
        audit.setUserId(dto.getUserId());
        audit.setActionType(dto.getAction());
        audit.setModuleAffected(dto.getModule());
        audit.setTargetKey(dto.getTargetKey());
        audit.setDescription(dto.getDescription());
        audit.setTimestamp(dto.getTimestamp());

        return audit;
    }

    public static AuditDTO toDTO(AuditLog log) {
        var dto = new AuditDTO();
        dto.setLogId(log.getLogId());
        dto.setUserId(log.getUserId());
        dto.setUsername(log.getUsername());
        dto.setAction(log.getActionType());
        dto.setModule(log.getModuleAffected());
        dto.setTargetKey(log.getTargetKey());
        dto.setDescription(log.getDescription());
        dto.setTimestamp(log.getTimestamp());

        return dto;
    }
}
