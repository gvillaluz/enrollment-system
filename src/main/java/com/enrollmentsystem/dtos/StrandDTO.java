package com.enrollmentsystem.dtos;

public class StrandDTO {
    private int strandId;
    private String strandCode;
    private String description;
    private int trackId;
    private String trackCode;

    public StrandDTO() {
        strandId = 0;
        strandCode = null;
        description = null;
        trackId = 0;
        trackCode = null;
    }

    public StrandDTO(int strandId, String strandCode, String description, int trackId, String trackCode) {
        this.strandId = strandId;
        this.strandCode = strandCode;
        this.description = description;
        this.trackId = trackId;
        this.trackCode = trackCode;
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
}
