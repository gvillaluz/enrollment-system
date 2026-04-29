package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.viewmodels.enrollment.requirements.NoteViewModel;
import com.enrollmentsystem.viewmodels.enrollment.requirements.RequirementNoteViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import java.time.format.DateTimeFormatter;

public class NoteListCell extends ListCell<NoteViewModel> {
    private final VBox noteItemContainer = new VBox();
    private final VBox noteContainer = new VBox();
    private final Label noteTitle = new Label();
    private final Label noteLabel = new Label();
    private final Label resolvedLabel = new Label("Resolved");
    private final HBox btnContainer = new HBox();

    private final Button undoBtn = new Button("Undo");
    private final Button editBtn = new Button("Edit");
    private final Button resolvedBtn = new Button("Mark as Resolved");

    private final RequirementNoteViewModel viewModel;

    public NoteListCell(RequirementNoteViewModel viewModel) {
        this.viewModel = viewModel;
        setupHierarchy();
        setupStyles();
        setupIcons();

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setMaxWidth(Double.MAX_VALUE);
        noteLabel.setWrapText(true);
        noteLabel.setMaxWidth(550);
    }

    private void setupHierarchy() {
        noteContainer.getChildren().addAll(noteTitle, noteLabel);
        noteContainer.setAlignment(Pos.CENTER_LEFT);
        noteContainer.setSpacing(10.0);
        noteContainer.setPadding(new Insets(5, 0, 5, 12));

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox.setMargin(resolvedLabel, new Insets(0, 0, 0, 10));

        btnContainer.getChildren().addAll(resolvedLabel, region, undoBtn, editBtn, resolvedBtn);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.setSpacing(10.0);
        btnContainer.setPadding(new Insets(5, 10, 5, 0));
        VBox.setVgrow(btnContainer, Priority.ALWAYS);

        noteItemContainer.getChildren().addAll(noteContainer, btnContainer);
        noteItemContainer.setMaxWidth(Double.MAX_VALUE);
    }

    private void setupStyles() {
        noteItemContainer.getStyleClass().add("note-item-container");
        noteContainer.getStyleClass().add("note-container");
        noteTitle.getStyleClass().add("note-title");
        noteLabel.getStyleClass().add("note");
        resolvedLabel.getStyleClass().add("resolved-lbl");

        undoBtn.getStyleClass().add("edit-btn");
        editBtn.getStyleClass().add("edit-btn");
        resolvedBtn.getStyleClass().add("resolved-btn");

        undoBtn.setMinWidth(80); undoBtn.setPrefWidth(80);
        editBtn.setMinWidth(70); editBtn.setPrefWidth(70);
        resolvedBtn.setMinWidth(140); resolvedBtn.setPrefWidth(140);
    }

    private void setupIcons() {
        undoBtn.setGraphic(new FontIcon("fas-undo"));
        editBtn.setGraphic(new FontIcon("fas-pencil-alt"));

        FontIcon checkIcon = new FontIcon("fas-check");
        FontIcon resolvedIcon = new FontIcon("fas-check");
        checkIcon.getStyleClass().add("check-icon");
        resolvedIcon.getStyleClass().add("resolved-icon");
        resolvedBtn.setGraphic(checkIcon);
        resolvedLabel.setGraphic(resolvedIcon);

        undoBtn.setGraphicTextGap(6);
        editBtn.setGraphicTextGap(6);
        resolvedBtn.setGraphicTextGap(6);
        resolvedLabel.setGraphicTextGap(4);
    }

    @Override
    protected void updateItem(NoteViewModel note, boolean empty) {
        super.updateItem(note, empty);

        noteLabel.textProperty().unbind();
        noteTitle.textProperty().unbind();

        undoBtn.visibleProperty().unbind();
        undoBtn.managedProperty().unbind();

        editBtn.visibleProperty().unbind();
        editBtn.managedProperty().unbind();

        resolvedBtn.visibleProperty().unbind();
        resolvedBtn.managedProperty().unbind();

        if (empty || note == null) {
            setGraphic(null);
        } else {
            noteLabel.textProperty().bind(note.noteProperty());
            noteTitle.textProperty().bind(
                    javafx.beans.binding.Bindings.createStringBinding(() -> {

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a");

                                String date = note.updatedAtProperty().get() != null
                                        ? note.updatedAtProperty().get().format(formatter)
                                        : "No Date";

                                String role = note.userRoleProperty().get() != null
                                        ? note.userRoleProperty().get().getValue()
                                        : "No Role";

                                String user = note.usernameProperty().get() != null
                                        ? note.usernameProperty().get()
                                        : "Unknown";

                                return String.format("[%s] %s: %s", date, role, user);

                            },
                            note.updatedAtProperty(),
                            note.userRoleProperty(),
                            note.usernameProperty()
                    ));

            applyResolutionUI(note.resolvedProperty().get());

            note.resolvedProperty().addListener((obs, oldVal, newVal) -> {
                applyResolutionUI(newVal);
            });

            undoBtn.visibleProperty().bind(note.resolvedProperty());
            undoBtn.managedProperty().bind(note.resolvedProperty());

            resolvedLabel.visibleProperty().bind(note.resolvedProperty());
            resolvedLabel.managedProperty().bind(note.resolvedProperty());

            editBtn.visibleProperty().bind(note.resolvedProperty().not());
            editBtn.managedProperty().bind(note.resolvedProperty().not());

            resolvedBtn.visibleProperty().bind(note.resolvedProperty().not());
            resolvedBtn.managedProperty().bind(note.resolvedProperty().not());

            editBtn.setOnAction(e -> viewModel.setEditNote(note));
            resolvedBtn.setOnAction(e -> viewModel.markAsResolved(note));
            undoBtn.setOnAction(e -> viewModel.undoResolved(note));

            setGraphic(noteItemContainer);
        }
    }

    private void refreshTitle(NoteViewModel note) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a");
        String date = note.updatedAtProperty().get().format(formatter);
        String role = note.userRoleProperty().get().getValue();
        String user = note.usernameProperty().get();
        noteTitle.setText(String.format("[%s] %s: %s", date, role, user));
    }

    private void applyResolutionUI(boolean isResolved) {
        if (isResolved) {
            if (!noteItemContainer.getStyleClass().contains("resolved")) {
                noteItemContainer.getStyleClass().add("resolved");
            }
        } else {
            noteItemContainer.getStyleClass().remove("resolved");
        }
    }
}
