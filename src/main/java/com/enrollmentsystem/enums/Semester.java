package com.enrollmentsystem.enums;

public enum Semester {
    FIRST("1st"),
    SECOND("2nd");

    private String sem;

    Semester(String sem) {
        this.sem = sem;
    }

    public String getSem() { return sem; }

    public static Semester fromString(String text) {
        for (Semester semester : Semester.values()) {
            if (semester.sem.equalsIgnoreCase(text)) {
                return semester;
            }
        }
        return FIRST;
    }
}
