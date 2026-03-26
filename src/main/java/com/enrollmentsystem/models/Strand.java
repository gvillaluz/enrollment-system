package com.enrollmentsystem.models;

public class Strand {
    private int strandId;
    private String strandCode;
    private String strandDescription;
    private int trackId;
    private String trackCode;
    private boolean isArchived;

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public String getStrandCode() { return strandCode; }
    public void setStrandCode(String strandCode) { this.strandCode = strandCode; }

    public String getStrandDescription() { return strandDescription; }
    public void setStrandDescription(String strandDescription) { this.strandDescription = strandDescription; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
