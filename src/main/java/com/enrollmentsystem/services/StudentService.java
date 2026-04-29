package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.StudentRecordDTO;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.SchoolYearRepository;
import com.enrollmentsystem.repositories.StudentRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.filters.StudentFilter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudentService {
    private final StudentRepository _repo;
    private final SchoolYearRepository _syRepo;

    public StudentService(StudentRepository repository, SchoolYearRepository syRepo) {
        _repo = repository;
        _syRepo = syRepo;
    }

    public CompletableFuture<Integer> getRecordCount(StudentFilter filter) {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Unauthorized access. Please log in.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var schoolYearId = _syRepo.getActiveSchoolYearId(conn);

                if (schoolYearId == null)
                    throw new IllegalArgumentException("Failed to get students.");

                return _repo.countRecords(conn, filter, schoolYearId);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to get row count.");
            }
        });
    }

    public CompletableFuture<List<StudentRecordDTO>> getLatest20Records(StudentFilter filter) {
        if (UserSession.getInstance() == null)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Unauthorized access. Please log in.")
            );

        return CompletableFuture.supplyAsync(() -> {
           try (Connection conn = DatabaseConnection.getConnection()) {
               var schoolYearId =  _syRepo.getActiveSchoolYearId(conn);

               if (schoolYearId == null)
                   throw new IllegalArgumentException("Failed to get students.");

               return _repo.getLatestStudentRecords(conn, filter, schoolYearId);
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               throw new RuntimeException("Failed to load records.");
           }
        });
    }
}
