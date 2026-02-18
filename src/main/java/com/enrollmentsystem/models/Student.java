package com.enrollmentsystem.models;

import java.time.LocalDate;
import java.util.Date;

public class Student {
    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private String extensionName;
    private LocalDate birthDate;
    private Integer age;
    private String sex;
    private String placeOfBirth;
    private String motherTongue;
    private Boolean isIpMember;
    private String ipCommunityName;
    private Boolean is4PsBeneficiary;
    private String householdId4Ps;
    private Boolean hasDisability;

    public Student(String LRN, String lastName, String firstName, String middleName,
                   String extensionName, LocalDate birthDate, Integer age, String sex,
                   String placeOfBirth, String motherTongue, Boolean isIpMember,
                   String ipCommunityName, Boolean is4PsBeneficiary,
                   String householdId4Ps, Boolean hasDisability) {
        this.lrn = LRN;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.extensionName = extensionName;
        this.birthDate = birthDate;
        this.age = age;
        this.sex = sex;
        this.placeOfBirth = placeOfBirth;
        this.motherTongue = motherTongue;
        this.isIpMember = isIpMember;
        this.ipCommunityName = ipCommunityName;
        this.is4PsBeneficiary = is4PsBeneficiary;
        this.householdId4Ps = householdId4Ps;
        this.hasDisability = hasDisability;
    }

    public String getLrn() { return lrn; }
    public void setLRN(String lrn) { this.lrn = lrn; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getExtensionName() { return extensionName; }
    public void setExtensionName(String extensionName) { this.extensionName = extensionName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getMotherTongue() { return motherTongue; }
    public void setMotherTongue(String motherTongue) { this.motherTongue = motherTongue; }

    public Boolean getIsIpMember() { return isIpMember; }
    public void setIsIpMember(Boolean isIpMember) { this.isIpMember = isIpMember; }

    public String getIpCommunityName() { return ipCommunityName; }
    public void setIpCommunityName(String ipCommunityName) { this.ipCommunityName = ipCommunityName; }

    public Boolean getIs4PsBeneficiary() { return is4PsBeneficiary; }
    public void setIs4PsBeneficiary(Boolean is4PsBeneficiary) { this.is4PsBeneficiary = is4PsBeneficiary; }

    public String getHouseholdId4Ps() { return householdId4Ps; }
    public void setHouseholdId4Ps(String householdId4Ps) { this.householdId4Ps = householdId4Ps; }

    public Boolean getHasDisability() { return hasDisability; }
    public void setHasDisability(Boolean hasDisability) { this.hasDisability = hasDisability; }
}
