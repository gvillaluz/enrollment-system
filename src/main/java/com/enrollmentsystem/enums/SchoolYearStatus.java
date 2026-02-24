package com.enrollmentsystem.enums;

public enum SchoolYearStatus {
    OPEN("Open"),
    CLOSED("Closed");

    private final String dbValue;

    SchoolYearStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static SchoolYearStatus fromString(String text) {
        for (SchoolYearStatus status : SchoolYearStatus.values()) {
            if (status.dbValue.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return CLOSED;
    }
}
