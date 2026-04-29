package com.enrollmentsystem.viewmodels.enrollment.addstudent;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.*;
import com.enrollmentsystem.services.EnrollmentService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnrollmentViewModel {
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final StringProperty searchValue = new SimpleStringProperty("");
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private int gradeLevel;
    private final int PAGE_LIMIT = 17;

    private final EnrollmentService _service = AppContext.getEnrollmentService();
    private final ObservableList<EnrollmentSummaryViewModel> enrollmentList = FXCollections.observableArrayList();

    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public IntegerProperty totalPagesProperty() { return totalPages; }
    public StringProperty searchValueProperty() { return searchValue; }
    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<EnrollmentSummaryViewModel> getEnrollmentList() {
        return enrollmentList;
    }

    public void loadData(int pageIndex) {
        isLoading.set(true);

        int offset = pageIndex * PAGE_LIMIT;

        CompletableFuture<Integer> countTask = _service.countRows(gradeLevel, searchValue.get());
        CompletableFuture<List<EnrollmentDTO>> enrollmentTask = _service.loadLatest20Enrollments(gradeLevel, searchValue.get(), offset);

        CompletableFuture.allOf(countTask, enrollmentTask).thenAccept(v -> {
            Integer totalRows = countTask.join();
            List<EnrollmentDTO> enrollmentDTOS = enrollmentTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));

                enrollmentList.setAll(enrollmentDTOS.stream()
                        .map(EnrollmentSummaryViewModel::new)
                        .toList());

                isLoading.set(false);
            });
        })
        .exceptionally(ex -> {
            Platform.runLater(() -> isLoading.set(false));
            return null;
        });
    }

    public CompletableFuture<EnrollmentFormDTO> getEditData(int enrollmentId) {
        return _service.loadEnrollmentData(enrollmentId);
    }

    public CompletableFuture<Boolean> archiveEnrollment(EnrollmentSummaryViewModel enrollment, int pageIndex) {
        int enrollmentId = enrollment.enrollmentIdProperty().get();
        String lrn = enrollment.lrnProperty().get();

        if (enrollmentId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("No enrollment record selected.")
            );

        return _service.archiveEnrollmentRecord(enrollmentId, lrn)
                .thenApply(success -> {
                    if (success) {
                        Platform.runLater(() -> loadData(pageIndex));
                    }
                    return success;
                });
    }
}
