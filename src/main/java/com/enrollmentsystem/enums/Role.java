package com.enrollmentsystem.enums;

public enum Role {
    ENROLLMENT_STAFF("Enrollment Staff"),
    ADMIN("Admin");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return ENROLLMENT_STAFF;
    }
}
