package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.TrackDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrackViewModel {
    private final IntegerProperty trackId;
    private final StringProperty trackCode;
    private final StringProperty description;

    public TrackViewModel(TrackDTO dto) {
        trackId = new SimpleIntegerProperty(dto.getTrackId());
        trackCode = new SimpleStringProperty(dto.getTrackCode());
        description = new SimpleStringProperty(dto.getDescription());
    }

    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
    public StringProperty descriptionProperty() { return description; }
}
