package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.dtos.AuditDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class AuditViewModel {
    private final IntegerProperty logId;
    private final IntegerProperty userId;
    private final StringProperty username;
    private final ObjectProperty<AuditAction> action;
    private final ObjectProperty<AuditModule> module;
    private final StringProperty description;
    private final ObjectProperty<LocalDateTime> timestamp;

    public AuditViewModel(AuditDTO dto) {
        this.logId = new SimpleIntegerProperty(dto.getLogId());
        this.userId = new SimpleIntegerProperty(dto.getUserId());
        this.username = new SimpleStringProperty(dto.getUsername());
        this.action = new SimpleObjectProperty<>(dto.getAction());
        this.module = new SimpleObjectProperty<>(dto.getModule());
        this.description = new SimpleStringProperty(dto.getDescription());
        this.timestamp = new SimpleObjectProperty<>(dto.getTimestamp());
    }

    // --- PROPERTY GETTERS (For TableView Binds) ---
    public IntegerProperty logIdProperty() { return logId; }
    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty usernameProperty() { return username; }
    public ObjectProperty<AuditAction> actionProperty() { return action; }
    public ObjectProperty<AuditModule> moduleProperty() { return module; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
}
