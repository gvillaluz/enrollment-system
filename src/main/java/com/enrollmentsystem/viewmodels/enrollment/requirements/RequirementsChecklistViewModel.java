package com.enrollmentsystem.viewmodels.enrollment.requirements;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import com.enrollmentsystem.services.RequirementService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RequirementsChecklistViewModel {
    private final StringProperty searchValue = new SimpleStringProperty("");
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final RequirementService _service = AppContext.getRequirementService();
    private final ObservableList<RequirementsSummaryViewModel> checklist = FXCollections.observableArrayList();

    public StringProperty searchValueProperty() { return searchValue; }
    public BooleanProperty loadingProperty() { return isLoading; }
    public IntegerProperty totalPagesProperty() { return totalPages; }

    public ObservableList<RequirementsSummaryViewModel> getChecklist() { return checklist; }

    private final int PAGE_LIMIT = 13;

    public void loadChecklist(int pageIndex) {
        isLoading.set(true);

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

                isLoading.set(false);
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

    public CompletableFuture<List<NoteViewModel>> getRequirementNotes(String lrn) {
        if (lrn != null) {
            return _service.getRequirementNotes(lrn)
                    .thenApply(requirementNoteDTOS -> {
                            return requirementNoteDTOS.stream()
                                    .map(NoteViewModel::new)
                                    .toList();
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
        } else {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid Record.")
            );
        }
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
