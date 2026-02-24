package com.enrollmentsystem.dtos;

public class TrackDTO {
    private int trackId;
    private String trackCode;
    private String description;

    public TrackDTO() {
        this.trackCode = "";
        this.description = "";
    }

    public TrackDTO(int trackId, String trackCode, String description) {
        this.trackId = trackId;
        this.trackCode = trackCode;
        this.description = description;
    }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
