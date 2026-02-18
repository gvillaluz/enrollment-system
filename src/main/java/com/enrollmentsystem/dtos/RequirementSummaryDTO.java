package com.enrollmentsystem.dtos;

public class RequirementSummaryDTO {
    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private Boolean beef;
    private Boolean sf9;
    private Boolean psa;
    private Boolean gmc;
    private Boolean au;
    private Boolean form5;
    private Boolean alsCol;

    public RequirementSummaryDTO(String lrn, String lastName, String firstName, String middleName,
                                 Boolean beef, Boolean sf9, Boolean psa, Boolean gmc, Boolean au, Boolean form5, Boolean alsCol) {
        this.lrn = lrn;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.beef = beef;
        this.sf9 = sf9;
        this.psa = psa;
        this.gmc = gmc;
        this.au = au;
        this.form5 = form5;
        this.alsCol = alsCol;
    }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName  = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public Boolean getBeef() { return beef; }
    public void setBeef(Boolean beef) { this.beef = beef; }

    public Boolean getSf9() { return sf9; }
    public void setSf9(Boolean sf9) { this.sf9 = sf9; }

    public Boolean getPsa() { return psa; }
    public void setPsa(Boolean psa) { this.psa = psa; }

    public Boolean getGmc() { return gmc; }
    public void setGmc(Boolean gmc) { this.gmc = gmc; }

    public Boolean getAu() { return au; }
    public void setAu(Boolean au) { this.au = au; }

    public Boolean getForm5() { return form5; }
    public void setForm5(Boolean form5) { this.form5 = form5; }

    public Boolean getAlsCol() { return alsCol; }
    public void setAlsCol(Boolean alsCol) { this.alsCol = alsCol; }
}
