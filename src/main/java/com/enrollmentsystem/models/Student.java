package com.enrollmentsystem.models;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.enums.Gender;

import java.time.LocalDate;
import java.util.Date;

public class Student {
    private String lrn;
    private String lastName;
    private String firstName;
    private String middleName;
    private String extensionName;
    private LocalDate birthDate;
    private int age;
    private Gender sex;

    public Student() {}

    public Student(String LRN, String lastName, String firstName, String middleName,
                   String extensionName, LocalDate birthDate, int age, Gender sex) {
        this.lrn = LRN;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.extensionName = extensionName;
        this.birthDate = birthDate;
        this.age = age;
        this.sex = sex;
    }

    public static Student fromDTO(EnrollmentFormDTO dto) {
        Student student = new Student();
        student.setLrn(dto.getLrn());
        student.setLastName(dto.getLastName());
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setExtensionName(dto.getExtensionName());
        student.setBirthDate(dto.getBirthDate());
        student.setSex(dto.getSex());
        student.setAge(dto.getAge());
        return student;
    }

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
}
