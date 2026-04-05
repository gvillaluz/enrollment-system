package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.AuditDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.services.AuditService;
import com.enrollmentsystem.utils.filters.AuditFilter;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuditTrailViewModel {
    private final ObjectProperty<AuditModule> moduleFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<AuditAction> actionFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateToFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateFromFilter = new SimpleObjectProperty<>();
    private final StringProperty usernameFilter = new SimpleStringProperty();
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final int PAGE_LIMIT = 15;

    private final ObservableList<AuditViewModel> auditList = FXCollections.observableArrayList();
    private final AuditService _service = AppContext.getAuditService();

    public ObservableList<AuditViewModel> getAuditList() { return auditList; }

    public ObjectProperty<AuditModule> moduleFilterProperty() { return moduleFilter; }
    public ObjectProperty<AuditAction> actionFilterProperty() { return  actionFilter; }
    public ObjectProperty<LocalDate> dateToFilterProperty() { return dateToFilter; }
    public ObjectProperty<LocalDate> dateFromFilterProperty() { return dateFromFilter; }
    public StringProperty usernameFilterProperty() { return usernameFilter; }
    public IntegerProperty totalPagesProperty() { return totalPages; }
    public BooleanProperty loadingProperty() { return isLoading; }

    public void loadAuditList(int pageIndex) {
        isLoading.set(true);

        LocalDateTime start = (dateFromFilter.get() != null) ? dateFromFilter.get().atStartOfDay() : null;
        LocalDateTime end = (dateToFilter.get() != null) ? dateToFilter.get().atTime(23, 59, 59) : null;

        var auditFilter = new AuditFilter(
                usernameFilter.get(),
                actionFilter.get(),
                moduleFilter.get(),
                start,
                end,
                pageIndex * PAGE_LIMIT
        );

        CompletableFuture<Integer> countTask = _service.getTotalCount(auditFilter);
        CompletableFuture<List<AuditDTO>> listTask = _service.getAllLogs(auditFilter);

        CompletableFuture.allOf(listTask, countTask).thenAccept(v -> {
            List<AuditDTO> auditDTOS = listTask.join();
            Integer totalRows = countTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));

                auditList.setAll(auditDTOS.stream()
                        .map(AuditViewModel::new)
                        .toList());

                isLoading.set(false);
            });
        })
        .exceptionally(ex -> {
            Platform.runLater(() -> isLoading.set(false));
            return null;
        });
    }

    public void clearFilters() {
        moduleFilter.set(null);
        actionFilter.set(null);
        dateFromFilter.set(null);
        dateToFilter.set(null);
        usernameFilter.set("");
    }
}
