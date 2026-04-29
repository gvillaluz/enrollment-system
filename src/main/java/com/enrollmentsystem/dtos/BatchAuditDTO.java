package com.enrollmentsystem.dtos;

import java.time.LocalDateTime;

public class BatchAuditDTO {
    private int schoolYearId;
    private String schoolYear;
    private String processedBy;
    private LocalDateTime processedAt;

    public BatchAuditDTO() {}

    public BatchAuditDTO(int schoolYearId, String schoolYear, String processedBy,
                         LocalDateTime processedAt) {
        this.schoolYearId = schoolYearId;
        this.schoolYear = schoolYear;
        this.processedBy = processedBy;
        this.processedAt = processedAt;
    }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getProcessedBy() { return processedBy; }
    public void setProcessedBy(String processedBy) { this.processedBy = processedBy; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
