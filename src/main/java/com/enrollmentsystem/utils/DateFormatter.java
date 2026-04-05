package com.enrollmentsystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final DateTimeFormatter LOG_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

    public static String formatAuditTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(LOG_FORMATTER);
    }
}
