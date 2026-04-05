package com.enrollmentsystem.utils.filters;

public class ModalConfig {
    private final String title;
    private final String message;
    private final Runnable onConfirm;

    public ModalConfig(String title, String message, Runnable onConfirm) {
        this.title = title;
        this.message = message;
        this.onConfirm = onConfirm;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Runnable getOnConfirm() { return onConfirm; }
}
