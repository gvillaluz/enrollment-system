package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.viewmodels.enrollment.requirements.NoteViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class NoteItemController {
    @FXML private VBox noteItemContainer;
    @FXML private Label noteTitle, noteLabel;
    @FXML private Button editBtn, resolvedBtn, undoBtn;

    private NoteViewModel note;
    private Consumer<NoteViewModel> editNote;
    private Consumer<NoteViewModel> markAsResolved;

    @FXML
    public void initialize() {
        setupButtons();
    }

    @FXML
    public void onEditNote() {
        if (editNote != null) {
            editNote.accept(note);
        }
    }

    @FXML
    public void onMarkAsResolved() {
        if (markAsResolved != null) {
            markAsResolved.accept(note);
        }
    }

    @FXML
    public void onUndo() {

    }

    public void setData(NoteViewModel note, Consumer<NoteViewModel> editNote, Consumer<NoteViewModel> markAsResolved) {
        this.note = note;
        this.editNote = editNote;
        this.markAsResolved = markAsResolved;

        note.updatedAtProperty().addListener((obs, oldVal, newVal) -> {
            String title = "[" + note.updatedAtProperty().get().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a")) + "] " + note.userRoleProperty().get().getValue() + ": " + note.usernameProperty().get();
            noteTitle.setText(title);
        });

        String title = "[" + note.updatedAtProperty().get().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a")) + "] " + note.userRoleProperty().get().getValue() + ": " + note.usernameProperty().get();
        noteTitle.setText(title);
        noteLabel.textProperty().bind(note.noteProperty());

        boolean isResolved = note.resolvedProperty().get();

        undoBtn.setVisible(isResolved);
        undoBtn.setManaged(isResolved);
        editBtn.setVisible(!isResolved);
        editBtn.setManaged(!isResolved);
        resolvedBtn.setVisible(!isResolved);
        resolvedBtn.setManaged(!isResolved);

        if (isResolved) {
            if (!noteItemContainer.getStyleClass().contains("resolved")) {
                noteItemContainer.getStyleClass().add("resolved");
            }
        }

        note.resolvedProperty().addListener((obs, oldVal, newVal) -> {
            undoBtn.setVisible(newVal);
            undoBtn.setManaged(newVal);
            editBtn.setVisible(!newVal);
            editBtn.setManaged(!newVal);
            resolvedBtn.setVisible(!newVal);
            resolvedBtn.setManaged(!newVal);

            if (newVal) {
                if (!noteItemContainer.getStyleClass().contains("resolved")) {
                    noteItemContainer.getStyleClass().add("resolved");
                }
            } else {
                noteItemContainer.getStyleClass().remove("resolved");
            }
        });
    }

    public void setupButtons() {
        FontIcon editIcon = new FontIcon("fas-pencil-alt");
        FontIcon checkIcon = new FontIcon("fas-check");
        FontIcon undoIcon = new FontIcon("fas-undo");

        checkIcon.getStyleClass().add("check-icon");

        editBtn.setGraphic(editIcon);
        resolvedBtn.setGraphic(checkIcon);
        undoBtn.setGraphic(undoIcon);

        editBtn.setGraphicTextGap(8);
        resolvedBtn.setGraphicTextGap(8);
        undoBtn.setGraphicTextGap(8);
    }

    private void updateStyle(boolean isResolved) {
        if (isResolved) {
            if (!noteItemContainer.getStyleClass().contains("resolved")) {
                noteItemContainer.getStyleClass().add("resolved");
            }
        } else {
            noteItemContainer.getStyleClass().remove("resolved");
        }
    }
}
