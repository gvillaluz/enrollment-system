package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentFormDTO {
    private int schoolYearId;
    private int gradeLevel;
    private int enrollmentId;

    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private String extensionName;
    private LocalDate birthDate;
    private int age;
    private Gender sex;

    private int academicInformationId;
    private Integer lastGradeLevel;
    private String lastSchoolYear;
    private String lastSchoolName;
    private String lastSchoolId;
    private Semester semester;
    private int trackId;
    private int strandId;
    private Integer sectionId;
    private String trackName;
    private String strandName;
    private String sectionName;

    private List<StudentRequirementDTO> requirementDTOS = new ArrayList<>();

    public EnrollmentFormDTO() {}

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getSchoolYearId() { return schoolYearId; }
    public void setSchoolYearId(int schoolYearId) { this.schoolYearId = schoolYearId; }

    public int getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getLrn() { return lrn; }
    public void setLrn(String lrn) { this.lrn = lrn; }

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

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Gender getSex() { return sex; }
    public void setSex(Gender sex) { this.sex = sex; }


    public int getAcademicInformationId() { return academicInformationId; }
    public void setAcademicInformationId(int academicInformationId) { this.academicInformationId = academicInformationId; }

    public Integer getLastGradeLevel() { return lastGradeLevel; }
    public void setLastGradeLevel(Integer lastGradeLevel) { this.lastGradeLevel = lastGradeLevel; }

    public String getLastSchoolYear() { return lastSchoolYear; }
    public void setLastSchoolYear(String lastSchoolYear) { this.lastSchoolYear = lastSchoolYear; }

    public String getLastSchoolName() { return lastSchoolName; }
    public void setLastSchoolName(String lastSchoolName) { this.lastSchoolName = lastSchoolName; }

    public String getLastSchoolId() { return lastSchoolId; }
    public void setLastSchoolId(String lastSchoolId) { this.lastSchoolId = lastSchoolId; }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }

    public int getStrandId() { return strandId; }
    public void setStrandId(int strandId) { this.strandId = strandId; }

    public Integer getSectionId() { return sectionId; }
    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }

    public String getTrackName() { return trackName; }
    public void setTrackCode(String trackName) { this.trackName = trackName; }

    public String getStrandName() { return strandName; }
    public void setStrandCode(String strandName) { this.strandName = strandName; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public List<StudentRequirementDTO> getRequirementDTOS() { return requirementDTOS; }
    public void setRequirementDTOS(List<StudentRequirementDTO> requirementDTOS) { this.requirementDTOS = requirementDTOS; }
}