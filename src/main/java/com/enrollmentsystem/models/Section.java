package com.enrollmentsystem.models;

public class Section {
    private int sectionId;
    private String name;
    private int maxCap;
    private int schoolYearId;
    private int strandId;
    private String roomAssignment;

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public int getMaxCap() { return maxCap; }
    public void setMaxCap(int maxCap) { this.maxCap = maxCap; }

    public String getRoomAssignment() { return roomAssignment; }
    public void setRoomAssignment(String roomAssignment) { this.roomAssignment = roomAssignment; }
}
