package com.enrollmentsystem.enums;

public enum EnrollmentStatus {
    ENROLLED("Enrolled"),
    ARCHIVE("Archive");

    private String status;

    EnrollmentStatus(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }

    public static EnrollmentStatus fromString(String text) {
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.status.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return ENROLLED;
    }
}
