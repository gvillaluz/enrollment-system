package com.enrollmentsystem.viewmodels.academic.schoolyear;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.services.SchoolYearService;
import javafx.beans.property.*;

import java.time.Year;
import java.util.concurrent.CompletableFuture;

public class SchoolYearFormViewModel {
    private final ObjectProperty<Integer> startYear = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> endYear = new SimpleObjectProperty<>();

    public SchoolYearViewModel originalSy;
    private Runnable onSaveSuccess;

    private final SchoolYearService _service = AppContext.getSchoolYearService();

    public SchoolYearFormViewModel() {
        startYear.addListener((obs, oldVal, newVal) -> {
            int currentYear = Year.now().getValue();

            if (newVal != null && (newVal >= currentYear && newVal <= currentYear + 1)) {
                endYear.set(newVal + 1);
            } else {
                endYear.set(null);
            }
        });
    }

    public ObjectProperty<Integer> startYearProperty() { return startYear; }
    public ObjectProperty<Integer> endYearProperty() { return endYear; }

    public void setOnSaveSuccess(Runnable onSaveSuccess) { this.onSaveSuccess = onSaveSuccess; }
    public void setOriginalSy(SchoolYearViewModel sy) {
        this.originalSy = sy;

        if (originalSy != null) {
            startYear.set(originalSy.startYearProperty().get());
            endYear.set(originalSy.endYearProperty().get());
        } else {
            startYear.set(null);
            endYear.set(null);
        }
    }

    public CompletableFuture<Boolean> saveSchoolYear() {
        Integer startYear = this.startYear.get();
        Integer endYear = this.endYear.get();

        if (startYear == null || endYear == null)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("School year fields cannot be empty. ")
            );

        SchoolYearDTO dto = new SchoolYearDTO();
        dto.setStartYear(startYear);
        dto.setEndYear(endYear);

        if (originalSy == null) {
            return _service.createSchoolYear(dto)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        } else {
            dto.setSchoolYearId(originalSy.schoolYearIdProperty().get());

            return _service.updateSchoolYear(dto)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        }
    }
}
