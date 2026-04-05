package com.enrollmentsystem.utils;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

public class StringFormatter {
    public static TextFormatter<Integer> formatStringToInteger(Integer defaultValue) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        };

        StringConverter<Integer> nullableConverter = new StringConverter<>() {
            @Override
            public String toString(Integer value) {
                // If value is null or 0 (and we want it null), show empty string
                if (value == null) return "";
                return value.toString();
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;
                return Integer.parseInt(string);
            }
        };

        return new TextFormatter<>(nullableConverter, null, filter);
    }
}
