package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.utils.ViewNavigator;
import com.enrollmentsystem.viewmodels.enrollment.requirements.NoteViewModel;
import com.enrollmentsystem.viewmodels.enrollment.requirements.RequirementNoteViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.function.Consumer;

public class RequirementNoteController {
    @FXML private BorderPane modal;
    @FXML private TextArea addNoteField;
    @FXML private ListView<NoteViewModel> noteList;

    @FXML private VBox addContainer;

    @FXML private Button clearBtn, addNote;

    private final RequirementNoteViewModel viewModel = new RequirementNoteViewModel();
    private final FontIcon addIcon = new FontIcon("fas-plus");

    private Consumer<Integer> onCloseCallback;

    @FXML
    public void initialize() {
        modal.setOnMouseClicked(event -> Platform.runLater(() -> modal.requestFocus()));

        setupButtons();
        setupList();

        addNoteField.textProperty().bindBidirectional(viewModel.noteProperty());

        viewModel.editingNoteIdProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != 0) {
                FontIcon saveIcon = new FontIcon("fas-save");
                saveIcon.getStyleClass().add("add-icon");
                addNote.setText("Save Note");
                addNote.setGraphic(saveIcon);
            } else {
                addNote.setText("Add Note");
                addNote.setGraphic(addIcon);
            }
        });
    }

    @FXML
    public void exitPopup(ActionEvent event) {
        if (onCloseCallback != null) {
            onCloseCallback.accept(viewModel.getUnresolvedCount());
            System.out.println(viewModel.getUnresolvedCount());
        }

        Button exitBtn = (Button) event.getSource();
        ViewNavigator.exitAddPopup(exitBtn);
    }

    @FXML
    public void saveNote(ActionEvent event) {
        viewModel.saveNote();
    }

    @FXML
    public void clearField(ActionEvent event) {
        viewModel.clearField();
    }

    public void setupButtons() {
        FontIcon clearIcon = new FontIcon("fas-brush");

        clearIcon.setRotate(180);

        addIcon.getStyleClass().add("add-icon");
        addNote.setGraphic(addIcon);
        addNote.setGraphicTextGap(10);

        clearBtn.setGraphic(clearIcon);
        clearBtn.setGraphicTextGap(10);
    }

    public void setNotes(List<NoteViewModel> notes, String lrn) { viewModel.setNotes(notes, lrn); }

    public void setOnCloseCallback(Consumer<Integer> onCloseCallback) { this.onCloseCallback = onCloseCallback; }

    public void setupList() {
        noteList.setItems(viewModel.getNotes());
        noteList.setCellFactory(listview -> new NoteListCell(viewModel));
        noteList.getStyleClass().add("list-view");

        Label placeholder = new Label("No notes found for this record.");
        placeholder.getStyleClass().add("placeholder-label");

        FontIcon infoIcon = new FontIcon("fas-info-circle");
        infoIcon.getStyleClass().add("placeholder-icon");
        placeholder.setGraphic(infoIcon);

        placeholder.setGraphicTextGap(10);

        noteList.setPlaceholder(placeholder);
    }
}

