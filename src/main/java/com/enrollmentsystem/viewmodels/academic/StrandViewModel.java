package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.StrandDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StrandViewModel {
    private final IntegerProperty strandId;
    private final StringProperty strandCode;
    private final StringProperty description;
    private final IntegerProperty trackId;
    private final StringProperty trackCode;

    public StrandViewModel(StrandDTO dto) {
        strandId = new SimpleIntegerProperty(dto.getStrandId());
        strandCode = new SimpleStringProperty(dto.getStrandCode());
        description = new SimpleStringProperty(dto.getDescription());
        trackId = new SimpleIntegerProperty(dto.getTrackId());
        trackCode = new SimpleStringProperty(dto.getTrackCode());
    }

    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public StringProperty descriptionProperty() { return description; }
    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
}
