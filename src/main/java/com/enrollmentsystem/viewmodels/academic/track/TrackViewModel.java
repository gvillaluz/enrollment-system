package com.enrollmentsystem.viewmodels.academic.track;

import com.enrollmentsystem.dtos.TrackDTO;
import javafx.beans.property.*;

public class TrackViewModel {
    private final IntegerProperty trackId;
    private final StringProperty trackCode;
    private final StringProperty description;
    private final BooleanProperty isArchived;

    public TrackViewModel(TrackDTO dto) {
        trackId = new SimpleIntegerProperty(dto.getTrackId());
        trackCode = new SimpleStringProperty(dto.getTrackCode());
        description = new SimpleStringProperty(dto.getDescription());
        isArchived = new SimpleBooleanProperty(dto.isArchived());
    }

    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
    public StringProperty descriptionProperty() { return description; }
    public BooleanProperty isArchivedProperty() { return isArchived; }

    @Override
    public String toString() {
        return trackCode.get();
    }
}
