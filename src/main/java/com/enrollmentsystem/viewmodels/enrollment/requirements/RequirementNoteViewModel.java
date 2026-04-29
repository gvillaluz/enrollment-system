package com.enrollmentsystem.viewmodels.enrollment.requirements;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.RequirementNoteDTO;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.services.RequirementService;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RequirementNoteViewModel {
    private final IntegerProperty editingNoteId = new SimpleIntegerProperty(0);
    private final StringProperty note = new SimpleStringProperty();

    private final RequirementService _service = AppContext.getRequirementService();

    private final ObservableList<NoteViewModel> requirementNotes = FXCollections.observableArrayList();
    private NoteViewModel editNote;
    private String lrn;

    public IntegerProperty editingNoteIdProperty() { return editingNoteId; }
    public StringProperty noteProperty() { return note; }

    public void setNotes(List<NoteViewModel> notes, String lrn) {
        requirementNotes.setAll(notes);
        sortNotes();
        this.lrn = lrn;
    }
    public ObservableList<NoteViewModel> getNotes() { return requirementNotes; }

    public void setEditNote(NoteViewModel note) {
        this.editNote = note;

        if (note != null) {
            editingNoteId.set(note.requirementNoteIdProperty().get());
            this.note.set(note.noteProperty().get());

            System.out.println(note.requirementNoteIdProperty().get());
        } else {
            editingNoteId.set(0);
            this.note.set(null);
            System.out.println("No Note");
        }
    }

    public void markAsResolved(NoteViewModel note) {
        _service.markAsResolvedNote(note.requirementNoteIdProperty().get(), lrn)
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            note.resolvedProperty().set(true);
                            sortNotes();

                        }
                    });
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    public void saveNote() {
        var note = new RequirementNoteDTO();
        note.setNote(this.note.get());
        note.setUserId(UserSession.getInstance().getUser().getUserId());
        note.setUsername(UserSession.getInstance().getUser().getUsername());
        note.setUserRole(UserSession.getInstance().getUser().getRole());

        if (editingNoteId.get() == 0) {
            note.setResolved(false);
            note.setLrn(lrn);

            _service.createRequirementNote(note)
                    .thenAccept(noteDTO -> {
                        Platform.runLater(() -> {
                            if (noteDTO != null) {
                                requirementNotes.add(new NoteViewModel(noteDTO));
                                sortNotes();
                                clearField();
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
        } else {
            note.setRequirementNoteId(editingNoteId.get());
            note.setResolved(editNote.resolvedProperty().get());
            note.setLrn(editNote.lrnProperty().get());
            note.setDateAdded(editNote.dateAddedProperty().get());

            if (note.getNote().equals(editNote.noteProperty().get())) {
                Platform.runLater(this::clearField);
                return;
            }

            _service.updateRequirementNote(note)
                    .thenAccept(noteDTO -> {
                        Platform.runLater(() -> {
                            if (noteDTO != null) {
                                editNote.noteProperty().set(noteDTO.getNote());
                                editNote.updatedAtProperty().set(noteDTO.getUpdatedAt());
                                editNote.userIdProperty().set(noteDTO.getUserId());
                                editNote.usernameProperty().set(UserSession.getInstance().getUser().getUsername());
                                editNote.userRoleProperty().set(UserSession.getInstance().getUser().getRole());
                                clearField();
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
        }
    }

    public void undoResolved(NoteViewModel note) {
        _service.undoNoteResolved(note.requirementNoteIdProperty().get(), lrn)
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            note.resolvedProperty().set(false);
                            sortNotes();
                        }
                    });
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    public void clearField() {
        editNote = null;
        editingNoteId.set(0);
        note.set(null);
    }

    private void sortNotes() {
        requirementNotes.sort((a, b) -> {
            boolean aResolved = a.resolvedProperty().get();
            boolean bResolved = b.resolvedProperty().get();

            if (aResolved != bResolved) {
                return aResolved ? 1 : -1;
            }

            return b.dateAddedProperty().get().compareTo(a.dateAddedProperty().get());
        });
    }

    public int getUnresolvedCount() {
        return (int) requirementNotes.stream()
                .filter(n -> !n.resolvedProperty().get())
                .count();
    }
}
