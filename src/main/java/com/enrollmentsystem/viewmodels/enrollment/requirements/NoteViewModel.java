package com.enrollmentsystem.viewmodels.enrollment.requirements;

import com.enrollmentsystem.dtos.RequirementNoteDTO;
import com.enrollmentsystem.enums.Role;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class NoteViewModel {
    private final IntegerProperty requirementNoteId = new SimpleIntegerProperty();
    private final StringProperty note = new SimpleStringProperty();
    private final BooleanProperty isResolved = new SimpleBooleanProperty();
    private final StringProperty lrn = new SimpleStringProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final ObjectProperty<Role> userRole = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateAdded = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> updatedAt = new SimpleObjectProperty<>();

    public NoteViewModel(RequirementNoteDTO dto) {
        requirementNoteId.set(dto.getRequirementNoteId());
        note.set(dto.getNote());
        isResolved.set(dto.isResolved());
        lrn.set(dto.getLrn());
        userId.set(dto.getUserId());
        username.set(dto.getUsername());
        userRole.set(dto.getUserRole());
        dateAdded.set(dto.getDateAdded());
        updatedAt.set(dto.getUpdatedAt());
    }

    public IntegerProperty requirementNoteIdProperty() { return requirementNoteId; }
    public StringProperty noteProperty() { return note; }
    public BooleanProperty resolvedProperty() { return isResolved; }
    public StringProperty lrnProperty() { return lrn; }
    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty usernameProperty() { return username; }
    public ObjectProperty<Role> userRoleProperty() { return userRole; }
    public ObjectProperty<LocalDateTime> dateAddedProperty() { return dateAdded; }
    public ObjectProperty<LocalDateTime> updatedAtProperty() { return updatedAt; }
}
