package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.*;
import com.enrollmentsystem.enums.EnrollmentStatus;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.services.EnrollmentService;
import com.enrollmentsystem.services.StrandService;
import com.enrollmentsystem.services.StudentService;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Filter;

public class EnrollmentViewModel {
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final StringProperty searchValue = new SimpleStringProperty("");

    private int gradeLevel;
    private final int PAGE_LIMIT = 15;

    private final EnrollmentService _service = AppContext.getEnrollmentService();
    private final ObservableList<EnrollmentSummaryViewModel> enrollmentList = FXCollections.observableArrayList();

    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public IntegerProperty totalPagesProperty() { return totalPages; }
    public StringProperty searchValueProperty() { return searchValue; }

    public ObservableList<EnrollmentSummaryViewModel> getEnrollmentList() {
        return enrollmentList;
    }

    public void loadData(int pageIndex) {
        enrollmentList.clear();

        int offset = pageIndex * PAGE_LIMIT;

        CompletableFuture<Integer> countTask = _service.countRows(gradeLevel, searchValue.get());
        CompletableFuture<List<EnrollmentDTO>> enrollmentTask = _service.loadLatest20Enrollments(gradeLevel, searchValue.get(), offset);

        CompletableFuture.allOf(countTask, enrollmentTask).thenAccept(v -> {
            int totalRows = countTask.join();
            List<EnrollmentDTO> enrollmentDTOS = enrollmentTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));
                
                List<EnrollmentSummaryViewModel> newItems = enrollmentDTOS.stream()
                        .map(EnrollmentSummaryViewModel::new)
                        .toList();

                enrollmentList.setAll(newItems);
            });
        });
    }

    public CompletableFuture<EnrollmentFormDTO> getEditData(int enrollmentId) {
        return _service.loadEnrollmentData(enrollmentId);
    }
}
