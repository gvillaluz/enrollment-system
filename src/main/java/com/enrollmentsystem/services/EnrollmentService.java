package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import com.enrollmentsystem.repositories.EnrollmentRepository;

import java.util.List;

public class EnrollmentService {
    private static EnrollmentRepository _repo;

    public EnrollmentService(EnrollmentRepository repository) { _repo = repository; }

    public List<EnrollmentSummaryDTO> getTopSummaries() {
        return _repo.getAllSummaries();
    }
}
