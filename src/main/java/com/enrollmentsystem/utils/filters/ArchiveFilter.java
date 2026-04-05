package com.enrollmentsystem.utils.filters;

public class ArchiveFilter {
    private int gradeLevel;
    private int schoolYearId;
    private String searchValue;
    private int offset;

    public ArchiveFilter(int gradeLevel, int schoolYearId, String searchValue, int offset) {
        this.gradeLevel = gradeLevel;
        this.schoolYearId = schoolYearId;
        this.searchValue = searchValue;
        this.offset = offset;
    }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getSearchValue() { return searchValue; }
    public void setSearchValue(String searchValue) { this.searchValue = searchValue; }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }
}
