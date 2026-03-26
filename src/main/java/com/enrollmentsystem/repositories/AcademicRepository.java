package com.enrollmentsystem.repositories;

import com.enrollmentsystem.models.AcademicInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AcademicRepository {
    public void addAcademicInformation(Connection conn, AcademicInformation academicInformation) throws SQLException {
        String query = "INSERT INTO academic_information (student_lrn, last_gr_lvl, last_schl_yr, last_schl_name, last_schl_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, academicInformation.getStudentLrn());

            if (academicInformation.getLastGradeLevel() != null) {
                statement.setInt(2, academicInformation.getLastGradeLevel());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, academicInformation.getLastSchoolYear());
            statement.setString(4, academicInformation.getLastSchoolName());
            statement.setString(5, academicInformation.getLastSchoolId());

            statement.executeUpdate();
        }
    }

    public void updateAcademicInformation(Connection conn, AcademicInformation academic) throws SQLException {
        String query = "UPDATE academic_information " +
                        "SET " +
                        "last_gr_lvl = ?, " +
                        "last_schl_yr = ?, " +
                        "last_schl_name = ?, " +
                        "last_schl_id = ?    " +
                        "WHERE academic_information_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (academic.getLastGradeLevel() != null) {
                statement.setInt(1, academic.getLastGradeLevel());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setString(2, academic.getLastSchoolYear());
            statement.setString(3, academic.getLastSchoolName());
            statement.setString(4, academic.getLastSchoolId());
            statement.setInt(5, academic.getAcademicInformationId());

            statement.executeUpdate();
        }
    }
}
