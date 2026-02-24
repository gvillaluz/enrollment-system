package com.enrollmentsystem.enums;

public enum AuditModule {
    ENROLLMENT("Enrollment"),
    REQUIREMENTS("Requirements"),
    STUDENT_RECORDS("Student Records"),
    CLASSLIST("Classlist"),
    TRACK_MANAGEMENT("Track Management"),
    STRAND_MANAGEMENT("Strand Management"),
    SECTION_MANAGEMENT("Section Manangement"),
    SCHOOL_YEAR_MODULE("School Year Module"),
    RUN_BATCH_SECTIONING("Run Batch Sectioning"),
    USER_MANAGEMENT("User Management"),
    ARCHIVE_RECORDS("Archive Records");

    private final String value;

    AuditModule(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static AuditModule fromString(String text) {
        for (AuditModule module : AuditModule.values()) {
            if (module.value.equalsIgnoreCase(text)) {
                return module;
            }
        }
        return null;
    }
}
