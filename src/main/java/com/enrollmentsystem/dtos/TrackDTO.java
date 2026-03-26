package com.enrollmentsystem.dtos;

public class TrackDTO {
    private int trackId;
    private String trackCode;
    private String description;
    private boolean isArchived;

    public TrackDTO() {}

    public TrackDTO(int trackId, String trackCode, String description, boolean isArchived) {
        this.trackId = trackId;
        this.trackCode = trackCode;
        this.description = description;
        this.isArchived = isArchived;
    }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
