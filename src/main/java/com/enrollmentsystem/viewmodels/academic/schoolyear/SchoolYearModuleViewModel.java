package com.enrollmentsystem.viewmodels.academic.schoolyear;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.mappers.SchoolYearMapper;
import com.enrollmentsystem.services.SchoolYearService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;

public class SchoolYearModuleViewModel {
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final ObservableList<SchoolYearViewModel> schoolYears = FXCollections.observableArrayList();
    private final SchoolYearService _service = AppContext.getSchoolYearService();

    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<SchoolYearViewModel> getSchoolYears() { return schoolYears; }

    public void loadSchoolYears() {
        isLoading.set(true);

        _service.getAllSchoolYears()
                .thenAccept(schoolYearDTOS -> {
                    schoolYears.setAll(schoolYearDTOS.stream()
                            .map(SchoolYearViewModel::new)
                            .toList());
                    isLoading.set(false);
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> isLoading.set(false));
                    return null;
                });
    }

    public CompletableFuture<Boolean> openSchoolYear(SchoolYearViewModel schoolYear) {
        return _service.openSchoolYear(SchoolYearMapper.vmToDTO(schoolYear))
                .thenApply(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            loadSchoolYears();
                        }
                    });
                    return success;
                });
    }
}
