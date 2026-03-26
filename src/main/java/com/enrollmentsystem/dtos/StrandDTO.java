package com.enrollmentsystem.dtos;

public class StrandDTO {
    private int strandId;
    private String strandCode;
    private String description;
    private int trackId;
    private String trackCode;
    private boolean isArchived;

    public StrandDTO() {}

    public StrandDTO(int strandId, String strandCode, String description, int trackId, String trackCode, boolean isArchived) {
        this.strandId = strandId;
        this.strandCode = strandCode;
        this.description = description;
        this.trackId = trackId;
        this.trackCode = trackCode;
        this.isArchived = isArchived;
    }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public String getStrandCode() { return strandCode; }
    public void setStrandCode(String strandCode) { this.strandCode = strandCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public String getTrackCode() { return trackCode; }
    public void setTrackCode(String trackCode) { this.trackCode = trackCode; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
