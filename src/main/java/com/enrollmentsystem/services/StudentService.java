package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.repositories.StudentRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudentService {
    private final StudentRepository _repo;

    public StudentService(StudentRepository repository) {
        _repo = repository;
    }
}
