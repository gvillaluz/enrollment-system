package com.enrollmentsystem.services;

import com.enrollmentsystem.repositories.AuditRepository;

public class AuditService {
    private final AuditRepository _repo;

    public AuditService(AuditRepository repo) {
        _repo = repo;
    }
}
