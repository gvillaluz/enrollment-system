package com.enrollmentsystem.viewmodels.academic.section;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.services.SectionService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SectionManagementViewModel {
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final ObservableList<SectionViewModel> sections = FXCollections.observableArrayList();
    private final SectionService _service = AppContext.getSectionService();

    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<SectionViewModel> getSections() { return sections; }

    public void loadSections() {
        isLoading.set(true);

        _service.getAllSections()
                .thenAccept(sectionDTOS -> {
                    Platform.runLater(() -> {
                        sections.setAll(sectionDTOS.stream()
                                .map(SectionViewModel::new)
                                .toList());

                        isLoading.set(false);
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> isLoading.set(false));
                    return null;
                });
    }
}
