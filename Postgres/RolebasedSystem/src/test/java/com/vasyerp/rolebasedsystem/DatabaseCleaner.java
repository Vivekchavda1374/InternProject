package com.vasyerp.rolebasedsystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCleaner {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/rolebasedsystem";
        String user = "postgres";
        String password = "postgres";
        String sqlFilePath = "dummy_data.sql";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {

            System.out.println("Connected to database at " + url);
            String sql = Files.readString(Path.of(sqlFilePath));

            // Execute the script
            stmt.execute(sql);
            System.out.println("Database cleanup and seed completed successfully.");

        } catch (SQLException e) {
            System.err.println("SQL failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("File read failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
