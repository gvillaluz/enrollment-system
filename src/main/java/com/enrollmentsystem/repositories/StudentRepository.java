package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private static Connection _conn;

    public StudentRepository(Connection connection) {
        _conn = connection;
    }

//    public List<Student> findTop20OrderByDateDesc() throws SQLException {
//        List<Student> students = new ArrayList<>();
//        String query = "";
//
//        try (PreparedStatement statement = _conn.prepareStatement(query)) {
//            try (ResultSet rs = statement.executeQuery()) {
//
//            }
//        }
//    }
}
