package com.enrollmentsystem.config;

import com.enrollmentsystem.repositories.EnrollmentRepository;
import com.enrollmentsystem.repositories.StudentRepository;
import com.enrollmentsystem.repositories.UserRepository;
import com.enrollmentsystem.services.AuthService;
import com.enrollmentsystem.services.EnrollmentService;
import com.enrollmentsystem.services.StudentService;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AppContext {
    private static UserRepository userRepository;
    private static StudentRepository studentRepository;
    private static EnrollmentRepository enrollmentRepository;

    private static AuthService authService;
    private static StudentService studentService;
    private static EnrollmentService enrollmentService;

    private static Connection connection;

    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DatabaseConnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Connection Error: ", e);
            }
        }
        return connection;
    }

    // Users
    private static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository(getConnection());
        }
        return userRepository;
    }

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(getUserRepository());
        }
        return authService;
    }

    // Enrollment
    public static EnrollmentRepository getEnrollmentRepository() {
        if (enrollmentRepository == null) {
            enrollmentRepository = new EnrollmentRepository(getConnection());
        }
        return enrollmentRepository;
    }

    public static EnrollmentService getEnrollmentService() {
        if (enrollmentService == null) {
            enrollmentService = new EnrollmentService(getEnrollmentRepository());
        }
        return enrollmentService;
    }

    // Students
    public static StudentRepository getStudentRepository() {
        if (studentRepository == null) {
            studentRepository = new StudentRepository(getConnection());
        }
        return studentRepository;
    }

    public static StudentService getStudentService() {
        if (studentService == null) {
            studentService = new StudentService(getStudentRepository());
        }
        return studentService;
    }
}
