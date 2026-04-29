package com.enrollmentsystem.viewmodels.admin.archive;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.ArchiveDTO;
import com.enrollmentsystem.dtos.SchoolYearDTO;
import com.enrollmentsystem.services.ArchiveService;
import com.enrollmentsystem.utils.filters.ArchiveFilter;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArchiveRecordsViewModel {
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final StringProperty searchValue = new SimpleStringProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final IntegerProperty schoolYearId = new SimpleIntegerProperty();

    private final int PAGE_LIMIT = 15;

    public ArchiveRecordsViewModel() {
        loadSchoolYears();
    }

    private final ArchiveService _service = AppContext.getArchiveService();
    private final ObservableList<SchoolYearDTO> schoolYearList = FXCollections.observableArrayList();
    private final ObservableList<ArchiveViewModel> archiveRecords = FXCollections.observableArrayList();

    public IntegerProperty totalPagesProperty() { return totalPages; }
    public StringProperty searchValueProperty() { return searchValue; }
    public BooleanProperty loadingProperty() { return isLoading; }
    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }

    public ObservableList<ArchiveViewModel> getArchiveRecords() { return archiveRecords; }
    public ObservableList<SchoolYearDTO> getSchoolYearList() { return schoolYearList; }

    public void loadSchoolYears() {
        _service.getSchoolYears()
                .thenAccept(schoolYearDTOS -> {
                    Platform.runLater(() -> schoolYearList.setAll(schoolYearDTOS));
                })
                .exceptionally(ex  -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    public void loadArchives(int pageIndex) {
        isLoading.set(true);

        var filter = new ArchiveFilter(
                gradeLevel.get(),
                schoolYearId.get(),
                searchValue.get(),
                pageIndex * PAGE_LIMIT
        );

        CompletableFuture<Integer> countTask = _service.getRowCount(filter);
        CompletableFuture<List<ArchiveDTO>> archiveTask = _service.getArchiveRecords(filter);

        CompletableFuture.allOf(countTask, archiveTask).thenAccept(v -> {
                    Integer totalRows = countTask.join();
                    List<ArchiveDTO> archiveDTOS = archiveTask.join();

                    Platform.runLater(() -> {
                        int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                        totalPages.set(Math.max(pages, 1));

                        archiveRecords.setAll(archiveDTOS.stream()
                                .map(ArchiveViewModel::new)
                                .toList());

                        isLoading.set(false);
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> isLoading.set(false));
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    public CompletableFuture<Boolean> restoreRecord(int archiveId, String lrn) {
        if (archiveId == 0 || lrn.isBlank())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid record.")
            );

        return _service.restoreRecord(archiveId, lrn);
    }

    public CompletableFuture<Boolean> permanentDeleteRecord(int archiveId, String lrn) {
        if (archiveId == 0 || lrn.isBlank())
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid record.")
            );

        return _service.deleteRecord(archiveId, lrn);
    }
}
