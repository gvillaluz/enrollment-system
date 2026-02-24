package com.enrollmentsystem.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() { return gender; }

    public static Gender fromString(String text) {
        for (Gender gender : Gender.values()) {
            if (gender.gender.equalsIgnoreCase(text)) {
                return gender;
            }
        }
        return MALE;
    }
}
