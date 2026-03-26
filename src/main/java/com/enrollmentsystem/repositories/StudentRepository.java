package com.enrollmentsystem.repositories;

import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.utils.DatabaseConnection;

import java.sql.*;

public class StudentRepository {
    public boolean checkStudentExistsByLRN(String lrn) {
        String query = "SELECT COUNT(*) FROM student WHERE student_lrn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to find lrn: " + e.getMessage());
            return false;
        }
    }

    public Student findStudentByLRN(String lrn) {
        String query = "SELECT * FROM student WHERE student_lrn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, lrn);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getString("student_lrn"),
                            rs.getString("last_name"),
                            rs.getString("first_name"),
                            rs.getString("middle_name"),
                            rs.getString("extension_name"),
                            rs.getDate("birthdate").toLocalDate(),
                            rs.getInt("age"),
                            Gender.fromString(rs.getString("sex"))
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to find lrn: " + e.getMessage());
            return null;
        }
    }

    public void addStudent(Connection conn, Student student) throws SQLException {
        String query = "INSERT INTO student (student_lrn, last_name, first_name, middle_name, extension_name, birthdate, age, sex) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, student.getLrn());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getFirstName());
            statement.setString(4, student.getMiddleName());
            statement.setString(5, student.getExtensionName());
            statement.setDate(6, Date.valueOf(student.getBirthDate()));
            statement.setInt(7, student.getAge());
            statement.setString(8, student.getSex().getGender());

            statement.executeUpdate();
        }
    }

    public void updateStudent(Connection conn, Student student) throws SQLException {
        String query = "UPDATE student " +
                        "SET " +
                        "last_name = ?, " +
                        "first_name = ?, " +
                        "middle_name = ?, " +
                        "extension_name = ?, " +
                        "birthdate = ?, " +
                        "age = ?, " +
                        "sex = ? " +
                        "WHERE student_lrn = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, student.getLastName());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getMiddleName());
            statement.setString(4, student.getExtensionName());
            statement.setDate(5, Date.valueOf(student.getBirthDate()));
            statement.setInt(6, student.getAge());
            statement.setString(7, student.getSex().getGender());
            statement.setString(8, student.getLrn());

            statement.executeUpdate();
        }
    }
}
