package com.enrollmentsystem.enums;

public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String status;

    UserStatus(String status) { this.status = status; }

    public String getStatus() { return status; }

    public static UserStatus fromString(String text) {
        for (UserStatus status : UserStatus.values()) {
            if (status.status.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return ACTIVE;
    }
}
