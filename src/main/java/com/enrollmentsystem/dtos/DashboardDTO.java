package com.enrollmentsystem.dtos;

public class DashboardDTO {
    private int schoolYearId;
    private String schoolYear;
    private int totalGrade11Section;
    private int totalGrade12Section;
    private int totalGrade11;
    private int totalGrade12;
    private int g11Male;
    private int g11Female;
    private int g12Male;
    private int g12Female;

    public DashboardDTO(int schoolYearId, String schoolYear, int totalGrade11Section, int totalGrade12Section, int totalGrade11, int totalGrade12, int g11Male, int g11Female, int g12Male, int g12Female) {
        this.schoolYearId = schoolYearId;
        this.schoolYear = schoolYear;
        this.totalGrade11Section = totalGrade11Section;
        this.totalGrade12Section = totalGrade12Section;
        this.totalGrade11 = totalGrade11;
        this.totalGrade12 = totalGrade12;
        this.g11Male = g11Male;
        this.g11Female = g11Female;
        this.g12Male = g12Male;
        this.g12Female = g12Female;
    }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public int getTotalGrade11Section() { return totalGrade11Section; }
    public void setTotalGrade11Section(int totalGrade11Section) { this.totalGrade11Section = totalGrade11Section; }

    public int getTotalGrade12Section() { return totalGrade12Section; }
    public void setTotalGrade12Section(int totalGrade12Section) { this.totalGrade12Section = totalGrade12Section; }

    public int getTotalGrade11() { return totalGrade11; }
    public void setTotalGrade11(int totalGrade11) { this.totalGrade11 = totalGrade11; }

    public int getTotalGrade12() { return totalGrade12; }
    public void setTotalGrade12(int totalGrade12) { this.totalGrade12 = totalGrade12; }

    public int getG11Male() { return g11Male; }
    public void setG11Male(int g11Male) { this.g11Male = g11Male; }

    public int getG11Female() { return g11Female; }
    public void setG11Female(int g11Female) { this.g11Female = g11Female; }

    public int getG12Male() { return g12Male; }
    public void setG12Male(int g12Male) { this.g12Male = g12Male; }

    public int getG12Female() { return g12Female; }
    public void setG12Female(int g12Female) { this.g12Female = g12Female; }
}
