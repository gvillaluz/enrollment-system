package com.enrollmentsystem.viewmodels.academic.section;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.services.SectionService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class SectionFormViewModel {
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final StringProperty sectionName = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();
    private final StringProperty roomAssignment = new SimpleStringProperty();

    private final ObservableList<StrandDTO> strands = FXCollections.observableArrayList();

    public SectionViewModel originalSection;
    private Runnable onSaveSuccess;
    private final SectionService _service = AppContext.getSectionService();

    public SectionFormViewModel() {
        initializeStrands();
    }

    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }
    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public StringProperty roomAssignmentProperty() { return roomAssignment; }

    public ObservableList<StrandDTO> getStrands() { return strands; }

    public void setOriginalSection(SectionViewModel section) {
        originalSection = section;

        if (originalSection != null) {
            sectionId.set(section.sectionIdProperty().get());
            sectionName.set(section.sectionNameProperty().get());
            strandId.set(section.strandIdProperty().get());
            strandCode.set(section.strandCodeProperty().get());
            roomAssignment.set(section.roomAssignmentProperty().get());
        } else {
            sectionId.set(0);
            sectionName.set("");
            strandId.set(0);
            strandCode.set("");
            roomAssignment.set("");
        }
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { this.onSaveSuccess = onSaveSuccess; }

    private void initializeStrands() {
        AppContext.getStrandService().loadStrands()
                .thenAccept(strandDTOS -> {
                    Platform.runLater(() -> {
                        strands.setAll(strandDTOS);
                    });
                })
                .exceptionally(ex -> {
                    System.out.println("Error in loading strands: " + ex.getMessage());
                    return null;
                });
    }

    public CompletableFuture<Boolean> saveSection() {
        String sectionName = this.sectionName.get();
        String roomAssign = roomAssignment.get();
        int strandId = this.strandId.get();

        if (ValidationHelper.isNullOrEmpty(sectionName) || ValidationHelper.isNullOrEmpty(roomAssign) || strandId == 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("All fields are required.")
            );

        var section = new SectionDTO();
        section.setSectionName(sectionName);
        section.setRoomAssignment(roomAssign);
        section.setStrandId(strandId);
        section.setSectionId(sectionId.get());

        return _service.updateSection(section)
                .thenApply(success -> {
                    Platform.runLater(() -> {
                        if (success || onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                    });
                    return success;
                });
    }

    public boolean hasChanges() {
        String sectionName = this.sectionName.get();
        String roomAssign = roomAssignment.get();
        int strandId = this.strandId.get();

        return sectionName.equals(originalSection.sectionNameProperty().get()) &&
                strandId == originalSection.strandIdProperty().get() &&
                roomAssign.equals(originalSection.roomAssignmentProperty().get());
    }
}
