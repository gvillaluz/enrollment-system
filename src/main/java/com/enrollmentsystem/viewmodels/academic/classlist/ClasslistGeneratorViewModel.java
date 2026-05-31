package com.enrollmentsystem.viewmodels.academic.classlist;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.services.ClasslistService;
import com.enrollmentsystem.services.SectionService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClasslistGeneratorViewModel {
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final StringProperty sectionName = new SimpleStringProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final BooleanProperty isSectionExists = new SimpleBooleanProperty();
    private final StringProperty schoolYear = new SimpleStringProperty();

    private final ObservableList<ClasslistRecordViewModel> classlist = FXCollections.observableArrayList();
    private final ObservableList<SectionDTO> sections = FXCollections.observableArrayList();

    private final ClasslistService _service = AppContext.getClasslistService();
    private final SectionService _secService = AppContext.getSectionService();

    public ClasslistGeneratorViewModel() {
        sectionExists();
        getSchoolYear();
    }

    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }
    public BooleanProperty loadingProperty() { return isLoading; }
    public BooleanProperty sectionExistsProperty() { return isSectionExists; }
    public StringProperty schoolYearProperty() { return schoolYear; }

    public ObservableList<ClasslistRecordViewModel> getClasslist() { return classlist; }
    public ObservableList<SectionDTO> getSections() { return sections; }

    private void sectionExists() {
        _service.checkSectionsExists()
                .thenAccept(exists -> {
                    isSectionExists.set(exists);
                    if (exists) {
                        loadSections();
                    }
                });
    }

    private void getSchoolYear() {
        _service.getActiveSchoolYear()
                .thenAccept(this.schoolYear::set);
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
        classlist.clear();
    }

    public CompletableFuture<Boolean> exportClasslist(File file) {
        List<ClasslistRecordDTO> dtos = classlist.stream()
                .map(cl -> {
                    return new ClasslistRecordDTO(
                            cl.lrnProperty().get(),
                            cl.lastNameProperty().get(),
                            cl.firstNameProperty().get(),
                            cl.middleNameProperty().get(),
                            cl.genderProperty().get(),
                            cl.gradeLevelProperty().get(),
                            cl.sectionIdProperty().get(),
                            cl.sectionNameProperty().get()
                    );
                })
                .sorted(Comparator.comparing(ClasslistRecordDTO::getGender)
                        .thenComparing(ClasslistRecordDTO::getLastName))
                .toList();

        return _service.exportRecord(dtos, file, sectionName.get(), schoolYear.get());
    }
}
