package com.enrollmentsystem.services;

import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.repositories.StudentRepository;

import java.util.List;

public class StudentService {
    private static StudentRepository _repo;

    public StudentService(StudentRepository repository) {
        _repo = repository;
    }

    public List<Student> get20Students() {
        return null;
    }
}
