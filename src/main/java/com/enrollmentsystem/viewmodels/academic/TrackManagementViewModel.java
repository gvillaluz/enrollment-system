package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.TrackDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TrackManagementViewModel {
    private final TrackViewModel trackViewModel = new TrackViewModel(new TrackDTO());
    private final ObservableList<TrackViewModel> tracks = FXCollections.observableArrayList();

    public TrackViewModel getNewTrackForm() { return trackViewModel; }
    public ObservableList<TrackViewModel> getTracks() { return tracks; }

    public void loadTracks() {
        var dummy1 = new TrackDTO(1, "Academics", "Academics");
        var dummy2 = new TrackDTO(2, "Tech-voc", "Technical Vocational");

        tracks.addAll(
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2),
                new TrackViewModel(dummy1),
                new TrackViewModel(dummy2)
        );
    }

    public void saveTrack() {
        System.out.println(trackViewModel.trackCodeProperty().get());
        System.out.println(trackViewModel.descriptionProperty().get());
        trackViewModel.trackCodeProperty().set("");
        trackViewModel.descriptionProperty().set("");
    }
}
