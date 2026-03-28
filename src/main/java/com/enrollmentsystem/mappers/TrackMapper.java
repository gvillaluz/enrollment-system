package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.models.Track;

public class TrackMapper {
    public static Track toNewModel(TrackDTO dto) {
        var track = new Track();
        track.setTrackCode(dto.getTrackCode());
        track.setTrackDescription(dto.getDescription());
        track.setArchived(false);

        return track;
    }

    public static Track toModel(TrackDTO dto) {
        var track = new Track();
        track.setTrackId(dto.getTrackId());
        track.setTrackCode(dto.getTrackCode());
        track.setTrackDescription(dto.getDescription());
        track.setArchived(dto.isArchived());

        return track;
    }

    public static TrackDTO toDTO(Track track) {
        var dto = new TrackDTO();
        dto.setTrackId(track.getTrackId());
        dto.setTrackCode(track.getTrackCode());
        dto.setDescription(track.getTrackDescription());
        dto.setArchived(track.isArchived());

        return dto;
    }
}
