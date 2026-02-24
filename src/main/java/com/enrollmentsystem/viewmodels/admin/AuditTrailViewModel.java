package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class AuditTrailViewModel {
    private final ObjectProperty<AuditModule> moduleFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<AuditAction> actionFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateToFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateFromFilter = new SimpleObjectProperty<>();
    private final StringProperty usernameFilter = new SimpleStringProperty();

    private final ObservableList<AuditViewModel> auditList = FXCollections.observableArrayList();

    public ObservableList<AuditViewModel> getAuditList() { return auditList; }

    public ObjectProperty<AuditModule> moduleFilterProperty() { return moduleFilter; }
    public ObjectProperty<AuditAction> actionFilterProperty() { return  actionFilter; }
    public ObjectProperty<LocalDate> dateToFilterProperty() { return dateToFilter; }
    public ObjectProperty<LocalDate> dateFromFilterProperty() { return dateFromFilter; }
    public StringProperty usernameFilterProperty() { return usernameFilter; }
}
