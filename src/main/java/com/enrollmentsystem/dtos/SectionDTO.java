package com.enrollmentsystem.dtos;

public class SectionDTO {
    private int sectionId;
    private String sectionName;
    private int maxCapacity;
    private int schoolYearId;
    private String schoolYear;
    private int strandId;
    private String strandCode;
    private String roomAssignment;


    public SectionDTO(int sectionId, String sectionName, int maxCapacity, int schoolYearId,
                      String schoolYear, int strandId, String strandCode, String roomAssignment) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.maxCapacity = maxCapacity;
        this.schoolYearId = schoolYearId;
        this.schoolYear = schoolYear;
        this.strandId = strandId;
        this.strandCode = strandCode;
        this.roomAssignment = roomAssignment;
    }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public String getStrandCode() { return strandCode; }
    public void setStrandCode(String strandCode) { this.strandCode = strandCode; }

    public String getRoomAssignment() { return roomAssignment; }
    public void setRoomAssignment(String roomAssignment) { this.roomAssignment = roomAssignment; }
}
