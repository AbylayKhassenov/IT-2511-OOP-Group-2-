package edu.aitu.oop3.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL =
            "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.dnlotyamwwbvulifoavz";
    private static final String PASSWORD = "!SPDBPJ1_2026"; // ← DATABASE PASSWORD
    private DatabaseConnection() {
        // no instances
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}