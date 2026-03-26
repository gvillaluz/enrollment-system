package com.enrollmentsystem.models;

public class Track {
    private int trackId;
    private String trackCode;
    private String trackDescription;
    private boolean isArchived;

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public String getTrackDescription() { return trackDescription; }
    public void setTrackDescription(String trackDescription) { this.trackDescription = trackDescription; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
