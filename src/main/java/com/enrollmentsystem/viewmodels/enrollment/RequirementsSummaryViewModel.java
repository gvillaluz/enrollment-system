package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.dtos.RequirementSummaryDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class RequirementsSummaryViewModel {
    private final StringProperty lrn;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final BooleanProperty beef;
    private final BooleanProperty sf9;
    private final BooleanProperty psa;
    private final BooleanProperty gmc;
    private final BooleanProperty au;
    private final BooleanProperty form5;
    private final BooleanProperty alsCoc;

    public RequirementsSummaryViewModel(RequirementSummaryDTO dto) {
        this.lrn = new SimpleStringProperty(dto.getLrn());
        this.lastName = new SimpleStringProperty(dto.getLastName());
        this.firstName = new SimpleStringProperty(dto.getFirstName());
        this.middleName = new SimpleStringProperty(dto.getMiddleName());
        this.beef = new SimpleBooleanProperty(dto.getBeef());
        this.sf9 = new SimpleBooleanProperty(dto.getSf9());
        this.psa = new SimpleBooleanProperty(dto.getPsa());
        this.gmc = new SimpleBooleanProperty(dto.getGmc());
        this.au = new SimpleBooleanProperty(dto.getAu());
        this.form5 = new SimpleBooleanProperty(dto.getForm5());
        this.alsCoc = new SimpleBooleanProperty(dto.getAlsCol());

        this.beef.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.sf9.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.psa.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.gmc.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.au.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.form5.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
        this.alsCoc.addListener((obs, oldVal, newVal) -> onChangeCheckbox());
    }

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public BooleanProperty beefProperty() { return beef; }
    public BooleanProperty sf9Property() { return sf9; }
    public BooleanProperty psaProperty() { return psa; }
    public BooleanProperty gmcProperty() { return gmc; }
    public BooleanProperty auProperty() { return au; }
    public BooleanProperty form5Property() { return form5; }
    public BooleanProperty alsCocProperty() { return alsCoc; }

    private void onChangeCheckbox() {
        System.out.println("Testing Checkboxes");
    }
}
