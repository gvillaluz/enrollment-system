package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.DashboardDTO;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.DashboardRepository;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DashboardService {
    private final DashboardRepository _repo;
    private final SchoolYearRepository _syRepo;

    public DashboardService(DashboardRepository repo, SchoolYearRepository syRepo) {
        _repo = repo;
        _syRepo = syRepo;
    }

    public CompletableFuture<DashboardDTO> getDashboardData() {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var schoolYearId = _syRepo.getActiveSchoolYearId(conn);
                if (schoolYearId == null) throw new RuntimeException("Failed to get active records");

                return _repo.getData(conn, schoolYearId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load data");
            }
        });
    }
}
