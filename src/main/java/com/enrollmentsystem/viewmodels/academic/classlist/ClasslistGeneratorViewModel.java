package com.enrollmentsystem.viewmodels.academic.classlist;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.services.ClasslistService;
import com.enrollmentsystem.services.SectionService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class ClasslistGeneratorViewModel {
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final BooleanProperty isSectionExists = new SimpleBooleanProperty();

    private final ObservableList<ClasslistRecordViewModel> classlist = FXCollections.observableArrayList();
    private final ObservableList<SectionDTO> sections = FXCollections.observableArrayList();

    private final ClasslistService _service = AppContext.getClasslistService();
    private final SectionService _secService = AppContext.getSectionService();

    public ClasslistGeneratorViewModel() {
         sectionExists();
    }

    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public BooleanProperty loadingProperty() { return isLoading; }
    public BooleanProperty sectionExistsProperty() { return isSectionExists; }

    public ObservableList<ClasslistRecordViewModel> getClasslist() { return classlist; }
    public ObservableList<SectionDTO> getSections() { return sections; }

    public void sectionExists() {
        _service.checkSectionsExists()
                .thenAccept(exists -> {
                    isSectionExists.set(exists);
                    if (exists) {
                        loadSections();
                    }
                });
    }

    public void loadSections() {
        _secService.getAllSections()
                .thenAccept(sectionDTOS -> {
                    if (!sectionDTOS.isEmpty()) {
                        Platform.runLater(() -> {
                            sections.setAll(sectionDTOS);
                        });
                    }
                });
    }

    public CompletableFuture<Boolean> loadClasslist() {
        if (gradeLevel.get() <= 0 || sectionId.get() <= 0)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Fields must not be empty."));

        isLoading.set(true);

        return _service.getClasslistRecords(gradeLevel.get(), sectionId.get())
                .thenApply(classlistRecordDTOS -> {
                    Platform.runLater(() -> {
                        classlist.setAll(classlistRecordDTOS.stream()
                                .map(ClasslistRecordViewModel::new)
                                .toList());

                        isLoading.set(false);
                    });

                    return true;
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> isLoading.set(false));
                    return false;
                });
    }

    public void clearFields() {
        gradeLevel.set(0);
        sectionId.set(0);
    }
}
