package com.enrollmentsystem.utils;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.IntStream;

public class ModernDatePickerSkin extends DatePickerSkin {
    public ModernDatePickerSkin(DatePicker control) {
        super(control);

        // Access the internal popup content
        Node popupContent = getPopupContent();

        // FIX 1: Cast to Pane instead of HBox to avoid ClassCastException
        Pane monthYearPane = (Pane) popupContent.lookup(".month-year-pane");

        if (monthYearPane != null) {
            setupModernHeader(monthYearPane, control);
        }
    }

    // FIX 2: Ensure the parameter is Pane (parent of HBox and BorderPane)
    private void setupModernHeader(Pane headerPane, DatePicker picker) {
        // 1. Create Dropdowns
        ComboBox<Month> monthCombo = new ComboBox<>();
        monthCombo.getItems().setAll(Month.values());

        ComboBox<Integer> yearCombo = new ComboBox<>();
        for (int i = LocalDate.now().getYear(); i >= 1990; i--) {
            yearCombo.getItems().add(i);
        }

        // Handle null values for birthdate safely
        LocalDate currentVal = (picker.getValue() != null) ? picker.getValue() : LocalDate.now();
        monthCombo.getSelectionModel().select(currentVal.getMonth());
        yearCombo.getSelectionModel().select(Integer.valueOf(currentVal.getYear()));

        // 2. Logic to update picker
        monthCombo.setOnAction(e -> updateDate(picker, monthCombo.getValue(), yearCombo.getValue()));
        yearCombo.setOnAction(e -> updateDate(picker, monthCombo.getValue(), yearCombo.getValue()));

        // 3. The Layout Fix
        HBox comboContainer = new HBox(5, monthCombo, yearCombo);
        comboContainer.setAlignment(Pos.CENTER);
        comboContainer.setSpacing(10);
        comboContainer.setPadding(new javafx.geometry.Insets(0, 5, 0, 5));

        // FIX 3: Check if it's a BorderPane (Standard in JavaFX 21)
        if (headerPane instanceof BorderPane bp) {
            // This replaces the middle text (labels) but keeps the navigation arrows!
            bp.getLeft().setVisible(false);
            bp.getLeft().setManaged(false);
            bp.getRight().setVisible(false);
            bp.getRight().setManaged(false);

            bp.setCenter(comboContainer);
        } else {
            // Fallback for older JavaFX versions
            headerPane.getChildren().clear();
            headerPane.getChildren().add(comboContainer);
        }
    }

    private void updateDate(DatePicker picker, Month m, Integer y) {
        LocalDate current = (picker.getValue() != null) ? picker.getValue() : LocalDate.now();
        int day = Math.min(current.getDayOfMonth(), m.length(java.time.Year.isLeap(y)));
        picker.setValue(LocalDate.of(y, m, day));
    }
}