package com.enrollmentsystem.utils;

public class ValidationHelper {
    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isBlank();
    }
}
