package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.services.RequirementService;
import com.enrollmentsystem.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RequirementsChecklistViewModel {
    private final StringProperty searchValue = new SimpleStringProperty("");
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);

    private final RequirementService _service = AppContext.getRequirementService();
    private final ObservableList<RequirementsSummaryViewModel> checklist = FXCollections.observableArrayList();

    public StringProperty searchValueProperty() { return searchValue; }

    public IntegerProperty totalPagesProperty() { return totalPages; }

    public ObservableList<RequirementsSummaryViewModel> getChecklist() { return checklist; }

    private final int PAGE_LIMIT = 12;

    public void loadChecklist(int pageIndex) {
        checklist.clear();

        int offset = pageIndex * PAGE_LIMIT;

        CompletableFuture<List<RequirementSummaryDTO>> listTask = _service.getRequirementsChecklist(searchValue.get(), offset);
        CompletableFuture<Integer> countTask = _service.getRowCount(searchValue.get());

        CompletableFuture.allOf(countTask, listTask).thenAccept(v -> {
            int totalRows = countTask.join();
            List<RequirementSummaryDTO> DTOs = listTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));

                List<RequirementsSummaryViewModel> newItems = DTOs.stream()
                        .map(dto -> {
                            RequirementsSummaryViewModel vm = new RequirementsSummaryViewModel(dto);
                            attachListeners(vm);
                            return vm;
                        })
                        .toList();

                checklist.setAll(newItems);
            });
        });
    }

    public void syncRequirement(String lrn, int refId, boolean isSubmitted) {
        _service.updateStudentRequirement(lrn, refId, isSubmitted)
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    private void attachListeners(RequirementsSummaryViewModel row) {
        String lrn = row.lrnProperty().get();
        row.beefProperty().addListener((o, old, val) -> syncRequirement(lrn, 1, val));
        row.sf9Property().addListener((o, old, val) -> syncRequirement(lrn, 2, val));
        row.psaProperty().addListener((o, old, val) -> syncRequirement(lrn, 3, val));
        row.gmcProperty().addListener((o, old, val) -> syncRequirement(lrn, 4, val));
        row.auProperty().addListener((o, old, val) -> syncRequirement(lrn, 5, val));
        row.form5Property().addListener((o, old, val) -> syncRequirement(lrn, 6, val));
        row.alsCocProperty().addListener((o, old, val) -> syncRequirement(lrn, 7, val));
    }
}
