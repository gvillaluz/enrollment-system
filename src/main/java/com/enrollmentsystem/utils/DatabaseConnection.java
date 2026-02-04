package com.enrollmentsystem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        File externalFile = new File("database.properties");

        try {
            if (externalFile.exists()) {
                System.out.println("Using external database properties.");
                try (FileInputStream fs = new FileInputStream(externalFile)) {
                    properties.load(fs);
                }
            } else {
                System.out.println("Using internal database properties.");

                try (InputStream is = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
                    properties.load(is);
                }
            }
        } catch (IOException e) {
            System.out.println("Error in loading database properties." + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
