package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.viewmodels.enrollment.EnrollmentSummaryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ArchiveViewModel {
    private final ObservableList<EnrollmentSummaryViewModel> archiveRecords = FXCollections.observableArrayList();

    public ObservableList<EnrollmentSummaryViewModel> getArchiveRecords() { return archiveRecords; }
}
