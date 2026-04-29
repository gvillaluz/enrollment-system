package com.enrollmentsystem.enums;

public enum AuditAction {
    ADD("Add"),
    ARCHIVE("Archive"),
    RESTORE("Restore"),
    UPDATE("Update"),
    DELETE("Delete"),
    LOGIN("Login"),
    LOGOUT("Logout"),
    RUN_BATCH_SECTIONING("Run Batch Sectioning");

    private String value;

    AuditAction(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static AuditAction fromString(String text) {
        for (AuditAction action : AuditAction.values()) {
            if (action.value.equalsIgnoreCase(text)) {
                return action;
            }
        }
        return null;
    }
}
